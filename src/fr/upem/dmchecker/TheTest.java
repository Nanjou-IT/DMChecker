package fr.upem.dmchecker;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.file.Paths;
import java.security.Permission;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: dr
 * Date: 11/28/13
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class TheTest {
    private final static PrintStream oo = System.out;
    private final static PrintStream ee = System.err;

    static class UnrollError extends Error {
        final int exitCode;

        UnrollError(int exitCode) {
            this.exitCode = exitCode;
        }
    }

    static {
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkPermission(Permission perm) {
             }
            @Override
            public void checkPermission(Permission perm, Object context) {
            }

            @Override
            public void checkExit(int status) {
                throw new UnrollError(status);
            }
        });
    }

    private ByteArrayOutputStream bo;
    private ByteArrayOutputStream be;


    @Before
    public void setup() {
        bo = new ByteArrayOutputStream(10000);
        System.setOut(new PrintStream(bo));
        be = new ByteArrayOutputStream(10000);
        System.setErr(new PrintStream(be));


    }

    @Test
    public void testMain() throws Exception {

        try {
            fr.upem.dmchecker.DmChecker.main(null);
            fail("no exit");
        } catch (UnrollError e) {
            org.junit.Assert.assertEquals(1, e.exitCode);
            // ce message est inadapté car il n'y a aucun repertoire dans l'archive mais c'est celui que l'on attend
            org.junit.Assert.assertEquals("Bad arguments\r\n", be.toString());
            // org.junit.Assert.assertEquals(bo.toString(),"\n");
        }
    }
    
    @Test
    public void testMainNoTop() throws Exception {
    	// "TestFiles/notop.zip"
        try {
            String[] args = new String[]{"-1","-O folder", "TestingFiles\\notop.zip"};
            fr.upem.dmchecker.DmChecker.main(args);
            fail("no exit");
        } catch (UnrollError e) {
            org.junit.Assert.assertEquals(1, e.exitCode);
            // ce message est inadapté car il n'y a aucun repertoire dans l'archive mais c'est celui que l'on attend
            org.junit.Assert.assertEquals("Archive with more than one top directory\r\n", be.toString());
        }
    }

    @Test
    public void testMainExiste() throws Exception {
        final String existename="bob";
        try {
            String[] args = new String[]{"-1","-x",existename,"TestingFiles\\notop.zip"};
            fr.upem.dmchecker.DmChecker.main(args);
            fail("no exit");
        } catch (UnrollError e) {
            org.junit.Assert.assertEquals(1, e.exitCode);
            // ce message est inadapté car il n'y a aucun repertoire dans l'archive mais c'est celui que l'on attend
            String[] spl=bo.toString().split("\n");
            
            org.junit.Assert.assertEquals(Messages.getOutputString("x", existename)+"\r",spl[0]);
            // ?
            // org.junit.Assert.assertEquals(Messages.getOutputString("e", "~"),spl[1]);
        }
    }

    @Test
    public void testExtractZipDeZip() throws Exception {
        final String extension=".class";
        try {
            String[] args = new String[]{"-2", "-e", extension, "TestingFiles\\lezipdezip.zip", "ProjetsEleves"};
            fr.upem.dmchecker.DmChecker.main(args);
            fail("no exit");
        } catch (UnrollError e) {
        	org.junit.Assert.assertEquals(1, e.exitCode);
        	// ce message est inadapté car il n'y a aucun repertoire dans l'archive mais c'est celui que l'on attend
            String[] spl=bo.toString().split("\n");
            
            org.junit.Assert.assertEquals(Messages.getOutputString("e", extension)+"\r",spl[0]);
            org.junit.Assert.assertEquals(10,spl.length);
        }
    }

}


