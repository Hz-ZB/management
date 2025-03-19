package com.teach.javafx.controller;
import com.teach.javafx.request.OptionItem;
import com.teach.javafx.util.CommonMethod;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HonourEditController {
    @FXML
    private ComboBox<OptionItem> personComboBox;
    private List<OptionItem> personList;
    @FXML
    private TextField markField;
    private HonourTableController honourTableController= null;
    private Integer honourId= null;
    @FXML
    public void initialize() {
    }

    @FXML
    public void okButtonClick(){
        Map<String,Object> data = new HashMap<>();
        OptionItem op;
        op = personComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("personId",Integer.parseInt(op.getValue()));
        }
        data.put("honourId",honourId);
        data.put("mark",markField.getText());
        honourTableController.doClose("ok",data);
    }
    @FXML
    public void cancelButtonClick(){
        honourTableController.doClose("cancel",null);
    }

    public void setHonourTableController(HonourTableController honourTableController) {
        this.honourTableController = honourTableController;
    }
    public void init(){
        personList =honourTableController.getPersonList();
        personComboBox.getItems().addAll(personList );
    }
    public void showDialog(Map data){
        if(data == null) {
            honourId = null;
            personComboBox.getSelectionModel().select(-1);
            personComboBox.setDisable(false);
            markField.setText("");
        }else {
            honourId = CommonMethod.getInteger(data,"honourId");
            personComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(personList, CommonMethod.getString(data, "honourId")));
            personComboBox.setDisable(true);
            markField.setText(CommonMethod.getString(data, "mark"));
        }
    }
}
