//package com.qpidhealth.qpid.search.controller;
//
//import static org.mockito.Mockito.doReturn;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import org.skife.jdbi.v2.DBI;
//import org.skife.jdbi.v2.Handle;
//
//@RunWith(MockitoJUnitRunner.class)
//public class PatientControllerTest {
//
//    private static Handle handle;
//
//    @InjectMocks
//    @Spy
//    private PatientController target;
//
//    @Before
//    public void before() throws ClassNotFoundException, IOException {
//        // setup in memory database
//        Class.forName("org.h2.Driver");
//        DBI dbi = new DBI("jdbc:h2:mem:test");
//        handle = dbi.open();
//        handle.execute("set mode MySQL;");
//
//        // run sql script to create our table
//        String sql = new String(Files.readAllBytes(Paths.get("./init_mysql.sql")));
//        handle.createScript(sql).executeAsSeparateStatements();
//
//        // mock (as spy) to return handle ( it is like a DB connection, but for the JDBI framework)
//        // doReturn(handle).when(target).
//
//        // insert some data
//        handle.execute("INSERT INTO patient (id,patientname) VALUES (1,'username1');");
//        handle.execute("INSERT INTO patient (id,patientname) VALUES (2,'username2');");
//    }
//
//    @After
//    public void after() {
//        // clean data
//        handle.execute("DROP ALL OBJECTS;");
//    }
//
//}
