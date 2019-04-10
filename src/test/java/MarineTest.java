import com.harium.marine.Marine;
import org.junit.Assert;
import org.junit.Test;

public class MarineTest {

    @Test
    public void testHost() {
        Assert.assertEquals("", Marine.host());
    }

    @Test
    public void testPort() {
        Assert.assertEquals(0, Marine.port());
    }

}
