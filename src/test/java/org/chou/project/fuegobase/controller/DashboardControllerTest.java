package org.chou.project.fuegobase.controller;

import org.chou.project.fuegobase.controller.dashboard.DashboardController;
import org.chou.project.fuegobase.middleware.AuthenticationFilter;
import org.chou.project.fuegobase.security.SecurityConfiguration;
import org.chou.project.fuegobase.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DashboardController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {AuthenticationFilter.class}),
        excludeAutoConfiguration = {SecurityConfiguration.class})

@WithMockUser(username = "admin", roles = "ADMIN")
public class DashboardControllerTest {

    public static final String STORAGE_JSON = """
            {"data":0.123}
            """;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DashboardService dashboardService;

    @Test
    public void get_storage_data() throws Exception {
        double expectedData = 0.123;
        when(dashboardService.getStorage(anyString())).thenReturn(expectedData);
        mockMvc.perform(
                        get("/api/v1/dashboard/storage/{id}", "123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(STORAGE_JSON));
    }
}
