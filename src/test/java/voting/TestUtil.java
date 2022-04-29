package voting;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.test.web.servlet.MvcResult;
import voting.util.JsonUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class TestUtil {

    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJson(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException, JsonProcessingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readListFromJson(MvcResult result, Class<T> clazz) throws IOException {
        return JsonUtil.readValues(getContent(result), clazz);
    }
}
