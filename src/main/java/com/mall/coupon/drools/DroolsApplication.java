package com.mall.coupon.drools;

import com.mall.coupon.drools.model.*;
import com.mall.coupon.drools.service.CouponBatchService;
import com.mall.coupon.drools.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@SpringBootApplication
public class DroolsApplication {


    public static void main(String[] args) throws Exception {
//        test1();
        enquiryTest();
//        SpringApplication.run(DroolsApplication.class, args);
    }

    public static void test1() throws Exception {
        KieSession kieSession = check(new String[]{getRule(), getRule2()});

        IrssetDroolsVo drools = new IrssetDroolsVo();
        drools.setSurpDayCnt(4);
        kieSession.insert(drools);
        int i = kieSession.fireAllRules();
        System.out.println("命中： " + i +"\n返回结果：" + drools.getMsg());
    }

    public static void enquiryTest() throws Exception {
        Order order = new Order();
        order.setId(1);
        order.setTotalAmount(99);
        Coupon coupon = new Coupon();
        coupon.setCouponBatchCode("SAVE8");
        coupon.setCouponType("5");    // discount
        coupon.setSubCouponType("2"); // 1: order; 2: sku
        coupon.setMinBuyAmount(0);
        coupon.setNominal(80);
        CouponBatchService.addCouponBatch(coupon);

        EnquiryResult result = new EnquiryResult();

        // sku
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(1, 10, 10, 2));
        OrderService.addOrderDetail(order.getId(), items);

        CouponBatchService couponBatchService = new CouponBatchService();
        for (OrderItem item: items) {
            KieSession kieSession = getSession();
            kieSession.setGlobal("couponBatchService", couponBatchService);
            kieSession.insert(item);
            kieSession.insert(coupon);
            kieSession.insert(result);
            int hit = kieSession.fireAllRules();

            log.info("order item hit {} rules", hit);
            if (hit > 0) {
                log.info("after order item, result is: {}", result);
            }
        }

        KieSession kieSession = getSession();
        kieSession.insert(order);
        kieSession.insert(coupon);
        kieSession.insert(result);
        int hit = kieSession.fireAllRules();
//         hit = kieSession.fireAllRules();

        log.info("hit {} rules", hit);
        log.info("enquiry result: {}", result);
    }


    /**
     * 不进行检查
     * @param rule
     * @return
     */
    public static KieSession getSession(String rule) {
        KieSession kieSession = null;
        try {
            KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            builder.add(ResourceFactory.newByteArrayResource(rule.getBytes("UTF-8")), ResourceType.DRL);
            InternalKnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
            Collection<KiePackage> packages = builder.getKnowledgePackages();
            knowledgeBase.addPackages(packages);
            kieSession = knowledgeBase.newKieSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kieSession;
    }


    private static Resource[] getRuleFiles() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources("classpath*:rules/"  + "**/*.drl");
    }


    private static KieSession getSession() throws Exception {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
//        String pkg = "src/main/resources/";
//        String pkg = "src/main/java/com/xwsoft/demo/drools/rules/";

        for (Resource file : getRuleFiles()) {
            log.info("rule file: " + file.getFilename());
            try {
                kfs.write(ResourceFactory.newClassPathResource("rules/" + file.getFilename(), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            for (Message msg : results.getMessages()) {
                log.error("drools script error info : " + msg.getText());
            }
            throw new Exception("drools script error");
        }

        KieSessionRepo.setKieContainer("coupon", kieServices.newKieContainer(KieServices.Factory.get().getRepository().getDefaultReleaseId()));

        return KieSessionRepo.getKieSession("coupon");
    }

    /**
     * 检查
     * @param sq
     * @return
     * @throws Exception
     */
    private static KieSession check(String[] sq) throws Exception   {
//        KieSessionRepo kieSession = new KieSessionRepo();
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        String pkg = "src/main/resources/";
//        String pkg = "src/main/java/com/xwsoft/demo/drools/rules/";

        for (Resource file : getRuleFiles()) {
            if ("rule1.drl".equals(file.getFilename())) {
                System.out.println("!!!rule file: " + file.getFilename());
                continue;
            }
            System.out.println("rule file: " + file.getFilename());
            try {
                kfs.write(ResourceFactory.newClassPathResource("rules/" + file.getFilename(), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int idx = 0;
        for (String rs: sq) {
            kfs.write(pkg + "test-" + idx + ".drl", rs);
            idx ++;
        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            for (Message msg : results.getMessages()) {
                System.out.println("drools script error info : " + msg.getText());
            }
            throw  new  Exception("drools script error");
        }

        KieSessionRepo.setKieContainer("test", kieServices.newKieContainer(KieServices.Factory.get().getRepository().getDefaultReleaseId()));

        return KieSessionRepo.getKieSession("test");
    }

    public static String getRule() {
        StringBuilder ruleSb = new StringBuilder();
        ruleSb.append("package rule_10003;\n");
        ruleSb.append("import com.xwsoft.demo.drools.IrssetDroolsVo\n");
        ruleSb.append("rule rule_10003 \n");
        ruleSb.append("when \n");
        ruleSb.append("$riskDroolsVo : IrssetDroolsVo(surpDayCnt>=2 && surpDayCnt<=10); \n");
        ruleSb.append("then \n");
        ruleSb.append("$riskDroolsVo.setMsg(\"命中了rule1\"); \n");
        ruleSb.append("end");

        System.out.println(ruleSb.toString());
        return ruleSb.toString();
    }

    public static String getRule2() {
        StringBuilder ruleSb = new StringBuilder();
        ruleSb.append("package com.xwsoft.demo.drools.rules;\n");
        ruleSb.append("import com.xwsoft.demo.drools.IrssetDroolsVo\n");
        ruleSb.append("rule rule_10002 \n");
        ruleSb.append("when \n");
        ruleSb.append("$riskDroolsVo : IrssetDroolsVo(surpDayCnt>=1 && surpDayCnt<=10); \n");
        ruleSb.append("then \n");
        ruleSb.append("$riskDroolsVo.setMsg(\"命中了rule2\"); \n");
        ruleSb.append("end");

        System.out.println(ruleSb.toString());
        return ruleSb.toString();
    }
}
