package de.ds.swa;

import static com.vaadin.event.ShortcutAction.KeyCode.ENTER;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import com.vaadin.event.ShortcutListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import io.rincl.Resources;
import io.rincl.Rincl;
import io.rincl.Rincled;

public class MyForm extends FormLayout implements Rincled {

   private Resources i18n;

   private DateFormat dateFormat;
   private NumberFormat numberFormat;

   private static final double PRICE = 1234.56;
   private TextField tfName;

   public MyForm() {
      initI18n();
      initLayout();
   }

   private void initI18n() {
      Locale locale = getLocaleFromSession();
      System.out.println("Locale got from VaadinSession: " + locale);
      Rincl.setLocale(locale);
      this.i18n = getResources();
      this.dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
      this.numberFormat = NumberFormat.getInstance(locale);
   }

   private Locale getLocaleFromSession() {
      return VaadinSession.getCurrent().getLocale();
   }

   public void initLayout() {

      initComponents();

      setMargin(true);
      setSpacing(true);
   }

   private void initComponents() {
      final Label laDate = new Label(dateFormat.format(new Date()));
      laDate.setCaption(i18n.getString("text.date.caption"));

      final Label laPrice = new Label(numberFormat.format(PRICE) + " €");
      laPrice.setCaption(i18n.getString("text.price.caption"));

      final Component cbLanguage = createLanguageSelectBox(i18n);

      tfName = createInputField();

      Button button = new Button(i18n.getString("button.caption"));
      button.addClickListener( e -> actionPerformed());

      addComponents(cbLanguage, laDate, laPrice, tfName, button);

      tfName.focus();
   }

   private void actionPerformed() {
      addComponent(new Label(i18n.getString("text.success", tfName.getValue())));
   }

   private Component createLanguageSelectBox(final Resources i18n) {
      final NativeSelect selLanguage = new NativeSelect(i18n.getString("select.language.caption"));
      selLanguage.setNullSelectionAllowed(false);

      selLanguage.addItem("de");
      selLanguage.setItemCaption("de", i18n.getString("language.de"));
      selLanguage.addItem("en");
      selLanguage.setItemCaption("en", i18n.getString("language.en"));

      String preselectedLanguage = getLocaleFromSession().getLanguage();
      System.out.println("preselected language: " + preselectedLanguage);
      selLanguage.setValue(preselectedLanguage);

      selLanguage.addValueChangeListener(e -> {
         String lang = (String) selLanguage.getValue();
         Locale locale = new Locale(lang);
         VaadinSession.getCurrent().setLocale(locale);
         initI18n();
         UI.getCurrent().getPage().reload();
      });

      return selLanguage;
   }

   private TextField createInputField() {
      TextField textField = new TextField();
      textField.setCaption(i18n.getString("text.type.name"));
      textField.addShortcutListener(new ShortcutListener("Enter", ENTER, null) {
         @Override
         public void handleAction(final Object sender, final Object target) {
            actionPerformed();
         }
      });
      return textField;
   }
}
