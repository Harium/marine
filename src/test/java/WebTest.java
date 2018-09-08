import com.harium.web.Web;
import org.junit.Assert;
import org.junit.Test;

public class WebTest {

    @Test
    public void testHost() {
        Assert.assertEquals("", Web.host());
    }

    @Test
    public void testPort() {
        Assert.assertEquals(0, Web.port());
    }

}
