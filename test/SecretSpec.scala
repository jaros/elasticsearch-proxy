import com.google.common.io.BaseEncoding
import org.specs2.mutable.Specification

class SecretSpec extends Specification {

  "adminAppSecret in base64" >> {
    "be equal to YWRtaW5BcHBTZWNyZXQ=" >> {
      BaseEncoding.base64().encode("adminAppSecret".getBytes) must beEqualTo("YWRtaW5BcHBTZWNyZXQ=")
    }
  }

}
