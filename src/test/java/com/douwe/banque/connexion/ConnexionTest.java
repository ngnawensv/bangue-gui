/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.connexion;

import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maurice PENKA
 */
public class ConnexionTest {
    
    public ConnexionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getCon method, of class Connexion.
     */
//    @Test
//    public void testGetCon() {
//        System.out.println("getCon");
//        Connection expResult = null;
//        Connection result = Connexion.getCon();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of instance method, of class Connexion.
     */
    @Test
    public void testInstance() throws ClassNotFoundException, SQLException {
        System.out.println("instance");
        Connexion result = Connexion.instance();
        Connexion expResult = Connexion.getIniqueInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
