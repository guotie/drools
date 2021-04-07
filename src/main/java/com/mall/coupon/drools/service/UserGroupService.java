package com.mall.coupon.drools.service;

// 模拟业务侧: 用户是否属于某个用户群

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserGroupService {
    private static Map<String, List<Integer>> userGroups = null;

    private static void setUserGroups(Map<String, List<Integer>> ug) {
        userGroups = ug;
    }

    private static void addUserGroups(String userType, String userId, List<Integer> groupIds) {
        if (null == userGroups) {
            userGroups = new HashMap<>();
        }
        List<Integer> group = userGroups.get(User.userIdentify(userType, userId));
        if (group == null) {
            group = new ArrayList<>();
        }
        group.addAll(groupIds);
    }


    // 模拟业务侧: 用户是否属于某个用户群
    public static boolean userBelongToGroup(String userType, String userId, List<Integer> groups) {
        List<Integer> userGroup = userGroups.get(User.userIdentify(userType, userId));
        if (userGroup == null) {
            return false;
        }
        userGroup.retainAll(groups); // groups.retainAll();
        return !userGroup.isEmpty();
    }
}
