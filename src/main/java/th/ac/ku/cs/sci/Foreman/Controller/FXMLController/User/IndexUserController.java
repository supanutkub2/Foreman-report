package th.ac.ku.cs.sci.Foreman.Controller.FXMLController.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import th.ac.ku.cs.sci.Foreman.Controller.FXMLController.Post.IndexPostController;
import th.ac.ku.cs.sci.Foreman.Controller.ModelController.SiteController;
import th.ac.ku.cs.sci.Foreman.Model.Site;
import th.ac.ku.cs.sci.Foreman.Session.UserSession;


import java.io.IOException;
import java.sql.Date;


@Controller
@FxmlView("index.fxml")
public class IndexUserController {

    private final SiteController siteController;
    private final Resource SITEFXML;
    private final Resource POSTFXML;
    private final ApplicationContext ac ;

    @FXML
    private TableView<Site> table ;

    @Autowired
    public IndexUserController(SiteController siteController,
                               @Value("classpath:templates/SiteFXML/createSite.fxml") Resource SITEFXML,
                               @Value("classpath:templates/PostFXML/index.fxml") Resource POSTFXML,
                               ApplicationContext ac) {
        this.siteController = siteController;
        this.SITEFXML = SITEFXML;
        this.POSTFXML = POSTFXML;
        this.ac = ac ;
    }

    public void initialize() {
        loadSite();
    }

    private void loadSite() {
        
        TableColumn<Site,String> SITE = new TableColumn<>("Site");
        TableColumn<Site,String> STATUS = new TableColumn<>("Status");
        TableColumn<Site,Date> DATE = new TableColumn<>("lastUpDate");

        SITE.setCellValueFactory(new PropertyValueFactory<>("name"));
        STATUS.setCellValueFactory(new PropertyValueFactory<>("status"));
        DATE.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        table.getColumns().addAll(SITE,STATUS,DATE);
        table.getItems().addAll(siteController.getAll());

    }

    public void handleBtnShow(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(POSTFXML.getURL());
            loader.setControllerFactory(ac::getBean);
            IndexPostController controller = ac.getBean(IndexPostController.class);
            controller.setStie(table.getSelectionModel().getSelectedItem());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Parent) loader.load()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBtnCreateSite(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(SITEFXML.getURL());
            loader.setControllerFactory(ac::getBean);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Parent) loader.load()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tableRefresh();
    }

    @FXML
    public void handleBtnExits(ActionEvent event) {
//        UserSession.getInstance(UserSession.getUserInstance()).clearUserSession();
        System.exit(0);
    }

    private void tableRefresh() {
        table.getItems().clear();
        table.getColumns().clear();
        loadSite();
    }
}