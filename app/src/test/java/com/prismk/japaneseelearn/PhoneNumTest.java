package com.prismk.japaneseelearn;

import com.prismk.japaneseelearn.activities.RegisterActivity;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2019/4/3
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class PhoneNumTest {
    @Test
    public void testIsMobile() {
        String phoneNumber = "15041119773";
        boolean mobile = RegisterActivity.isMobileNO(phoneNumber);
        System.out.println("phoneNum is:" + mobile);
    }
}
