package bbojk.sideprojectplatformbackend;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public final class ApiDocumentUtils {
    public static OperationRequestPreprocessor preprocessDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme("https")
                        .host("docs.api.com")
                        .removePort(),
                prettyPrint());
    }

    public static OperationResponsePreprocessor preprocessDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }
}
