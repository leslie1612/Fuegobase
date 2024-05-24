package org.chou.project.fuegobase;

import org.chou.project.fuegobase.controller.dashboard.DashboardController;
import org.chou.project.fuegobase.controller.database.CollectionController;
import org.chou.project.fuegobase.controller.database.DocumentController;
import org.chou.project.fuegobase.controller.database.FieldController;
import org.chou.project.fuegobase.controller.database.ProjectController;
import org.chou.project.fuegobase.controller.security.APIKeyController;
import org.chou.project.fuegobase.controller.security.SettingsController;
import org.chou.project.fuegobase.controller.user.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FuegobaseApplicationTests {
    @Autowired
    private DashboardController dashboardController;

    @Autowired
    private CollectionController collectionController;

    @Autowired
    private DocumentController documentController;

    @Autowired
    private FieldController fieldController;

    @Autowired
    private ProjectController projectController;

    @Autowired
    private APIKeyController apiKeyController;

    @Autowired
    private SettingsController settingsController;

    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {
        assertThat(dashboardController).isNotNull();
        assertThat(collectionController).isNotNull();
        assertThat(documentController).isNotNull();
        assertThat(fieldController).isNotNull();
        assertThat(projectController).isNotNull();
        assertThat(apiKeyController).isNotNull();
        assertThat(settingsController).isNotNull();
        assertThat(userController).isNotNull();

    }

}
