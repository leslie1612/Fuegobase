package org.chou.project.fuegobase;

import org.chou.project.fuegobase.controller.dashboard.DashboardController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FuegobaseApplicationTests {
    @Autowired
    private DashboardController dashboardController;

    @Test
    void contextLoads() {
        assertThat(dashboardController).isNotNull();
    }

}
