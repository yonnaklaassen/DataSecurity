import ServerApplication.Model.Printer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import ServerApplication.Services.PrintService;
import org.junit.Test;

import java.util.Map;

public class PrintServiceTest {

    @Test
    public void testFindPrinter() {
        //arrange
        PrintService ps = new PrintService();
        String realName = "Printer #1";
        String fakeName = "Printer1";

        //act
        Printer p1 = ps.findPrinter(realName);
        Printer p2 = ps.findPrinter(fakeName);

        //assert
        assertEquals(realName, p1.getName());
        assertNull(p2);
    }

    @Test
    public void testPrint() {
        //arrange
        PrintService ps = new PrintService();
        String fileName = "test-file.txt";
        String printerName = "Printer #1";

        //act
        ps.print(fileName, printerName);

        //assert
        Printer p = ps.findPrinter(printerName);
        String queue = p.getQueue();
        String expected = "<0> <test-file.txt>\n";

        assertEquals(expected, queue);
    }

    @Test
    public void testAddToTopOfQueue() {
        //arrange
        PrintService ps = new PrintService();
        String fileName1 = "test-file1.txt"; //print job 0
        String fileName2 = "test-file2.txt"; //print job 1
        String printerName = "Printer #1";

        //act
        ps.print(fileName1, printerName);
        ps.print(fileName2, printerName);
        ps.topQueue(printerName, 1);

        //assert
        Printer p = ps.findPrinter(printerName);
        String queue = p.getQueue();
        String expected = "<1> <test-file2.txt>\n<0> <test-file1.txt>\n";

        assertEquals(expected, queue);
    }

    @Test
    public void testSetConfig() {
        //arrange
        PrintService ps = new PrintService();
        String parameter = "size";
        String value = "A4";

        //act
        ps.setConfig(parameter, value);
        String newValue = "A5";
        ps.setConfig(parameter, newValue);

        //assert
        Map<String, String> config = ps.getConfigurations();
        String parameterValue = config.get(parameter);

        assertEquals(newValue, parameterValue);
    }
}
