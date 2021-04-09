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
        enquiryTest();
//        SpringApplication.run(DroolsApplication.class, args);
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

    private static Resource[] getRuleFiles() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources("classpath*:rules/"  + "**/*.drl");
    }


    private static KieSession getSession() throws Exception {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
//        String pkg = "src/main/resources/";
//        String pkg = "src/main/java/com/mall/coupon/drools/rules/";

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

//        return kieServices.newKieContainer(KieServices.Factory.get().getRepository().getDefaultReleaseId()).newKieSession();

        KieSessionRepo.setKieContainer("coupon", kieServices.newKieContainer(KieServices.Factory.get().getRepository().getDefaultReleaseId()));

        return KieSessionRepo.getKieSession("coupon");
    }

}
