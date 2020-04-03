package com.rainlf;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

/**
 * @author : rain
 * @date : 2020/4/3 13:19
 */
@Slf4j
@Data
@SuppressWarnings("unchecked")
public class AccountPersistServiceImpl implements AccountPersistService {

    private String file;

    private SAXReader reader = new SAXReader();

    private String ELEMENT_ROOT = "ELEMENT_ROOT";
    private String ELEMENT_ACCOUNTS = "ELEMENT_ACCOUNTS";
    private String ELEMENT_ACCOUNT = "ELEMENT_ACCOUNT";
    private String ELEMENT_ACCOUNT_ID = "ELEMENT_ACCOUNT_ID";
    private String ELEMENT_ACCOUNT_NAME = "ELEMENT_ACCOUNT_NAME";
    private String ELEMENT_ACCOUNT_EMAIL = "ELEMENT_ACCOUNT_EMAIL";
    private String ELEMENT_ACCOUNT_PASSWORD = "ELEMENT_ACCOUNT_PASSWORD";
    private String ELEMENT_ACCOUNT_ACTIVATED = "ELEMENT_ACCOUNT_ACTIVATED";

    @Override
    public Account createAccount(Account account) throws AccountPersistException {
        Document doc = readDocument();

        Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
        accountsEle.add(buildElement(account));
        writeDocument(doc);

        return account;
    }

    @Override
    public Account readAccount(String id) throws AccountPersistException {
        Document doc = readDocument();

        Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
        for (Element accountEle : (List<Element>) accountsEle.elements()) {
            if (accountEle.elementText(ELEMENT_ACCOUNT_ID).equals(id)) {
                return buildAccount(accountEle);
            }
        }

        return null;
    }

    @Override
    public Account updateAccount(Account account) throws AccountPersistException {
        Document doc = readDocument();

        Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
        for (Element accountEle : (List<Element>) accountsEle.elements()) {
            if (accountEle.elementText(ELEMENT_ACCOUNT_ID).equals(account.getId())) {
                accountEle.element(ELEMENT_ACCOUNT_NAME).setText(account.getName());
                accountEle.element(ELEMENT_ACCOUNT_EMAIL).setText(account.getEmail());
                accountEle.element(ELEMENT_ACCOUNT_PASSWORD).setText(account.getPassword());
                accountEle.element(ELEMENT_ACCOUNT_ACTIVATED).setText(account.isActivated() ? "true" : "false");
            }
        }
        writeDocument(doc);

        return account;
    }

    @Override
    public void deleteAccount(String id) throws AccountPersistException {
        Document doc = readDocument();

        Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
        for (Element accountEle : (List<Element>) accountsEle.elements()) {
            if (accountEle.elementText(ELEMENT_ACCOUNT_ID).equals(id)) {
                accountsEle.remove(accountEle);
            }
        }
        writeDocument(doc);
    }

    private Document readDocument() throws AccountPersistException {
        File dataFile = new File(getFilePath());
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();

            Document doc = DocumentFactory.getInstance().createDocument();
            Element rootEle = doc.addElement(ELEMENT_ROOT);
            rootEle.addElement(ELEMENT_ACCOUNTS);
            writeDocument(doc);
        }

        try {
            return reader.read(new File(getFilePath()));
        } catch (DocumentException e) {
            throw new AccountPersistException("Unable to read persist data xml", e);
        }
    }

    private void writeDocument(Document doc) throws AccountPersistException {
        try (Writer out = new OutputStreamWriter(new FileOutputStream(getFilePath()), StandardCharsets.UTF_8)) {
            XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
            writer.write(doc);
        } catch (IOException e) {
            throw new AccountPersistException("Unable to write persis data xml", e);
        }
    }

    private Account buildAccount(Element element) {
        Account account = new Account();

        account.setId(element.elementText(ELEMENT_ACCOUNT_ID));
        account.setName(element.elementText(ELEMENT_ACCOUNT_NAME));
        account.setEmail(element.elementText(ELEMENT_ACCOUNT_EMAIL));
        account.setPassword(element.elementText(ELEMENT_ACCOUNT_PASSWORD));
        account.setActivated("true".equals(element.elementText(ELEMENT_ACCOUNT_ACTIVATED)));
        return account;
    }

    private Element buildElement(Account account) {
        Element element = new DOMElement(ELEMENT_ACCOUNT);

        Element idElement = new DOMElement(ELEMENT_ACCOUNT_ID);
        idElement.setText(account.getId());

        Element nameElement = new DOMElement(ELEMENT_ACCOUNT_NAME);
        nameElement.setText(account.getName());

        Element emailElement = new DOMElement(ELEMENT_ACCOUNT_EMAIL);
        emailElement.setText(account.getEmail());

        Element passwordElement = new DOMElement(ELEMENT_ACCOUNT_PASSWORD);
        passwordElement.setText(account.getPassword());

        Element activatedElement = new DOMElement(ELEMENT_ACCOUNT_ACTIVATED);
        activatedElement.setText(account.isActivated() ? "true" : "false");

        element.add(idElement);
        element.add(nameElement);
        element.add(emailElement);
        element.add(passwordElement);
        element.add(activatedElement);
        return element;
    }

    private String getFilePath() {
        return file;
    }
}
