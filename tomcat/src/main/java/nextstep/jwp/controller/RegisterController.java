package nextstep.jwp.controller;

import java.util.Map;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.exception.MethodNotAllowedException;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.request.startline.Extension;
import org.apache.coyote.http11.request.startline.Path;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpStatus;
import org.apache.support.ResourceFindUtils;

public class RegisterController implements Controller {

    private static final String REDIRECT_PATH = "/index.html";

    @Override
    public HttpResponse service(HttpRequest request) {
        if (request.isGetMethod()) {
            return doGet(request);
        }
        if (request.isPostMethod()) {
            return doPost(request);
        }

        throw new MethodNotAllowedException();
    }

    private HttpResponse doGet(HttpRequest request) {
        final Path path = request.getPath();
        final String responseBody = ResourceFindUtils
                .getResourceFile(path.getResource() + Extension.HTML.getExtension());
        return new HttpResponse.Builder()
                .status(HttpStatus.OK)
                .contentType(path.getContentType())
                .responseBody(responseBody)
                .build();
    }

    private HttpResponse doPost(HttpRequest request) {
        final Map<String, String> params = request.getBody();
        final String account = params.get("account");
        final String password = params.get("password");
        final String email = params.get("email");
        final User user = new User(account, password, email);

        InMemoryUserRepository.save(user);

        return new HttpResponse.Builder()
                .status(HttpStatus.FOUND)
                .location(REDIRECT_PATH)
                .build();
    }
}
