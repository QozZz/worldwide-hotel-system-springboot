//package com.qozz.worldwidehotelsystem.config.security;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@Transactional
//public class SecurityConfigurationTest {
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Before
//    public void setUp() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }
//
//    @Test
//    public void accessForAnonymousToAdminPageDenied() throws Exception {
//        mockMvc.perform(get("/admin/users"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithUserDetails(value = "user")
//    public void accessForUserToAdminPageDenied() throws Exception {
//        mockMvc.perform(get("/admin/users"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithUserDetails(value = "moderator")
//    public void accessForModeratorToAdminPageDenied() throws Exception {
//        mockMvc.perform(get("/admin/users"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithUserDetails(value = "admin")
//    public void accessForAdminToAdminPage() throws Exception {
//        mockMvc.perform(get("/admin/users"))
//                .andExpect(status().isOk());
//    }
//}
