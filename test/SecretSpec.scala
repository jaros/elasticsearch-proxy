import com.google.common.io.BaseEncoding
import org.specs2.mutable.Specification

class SecretSpec extends Specification {

  "in base64" >> {
    "adminAppSecret equals to YWRtaW5BcHBTZWNyZXQ=" >> {
      BaseEncoding.base64().encode("adminAppSecret".getBytes) must beEqualTo("YWRtaW5BcHBTZWNyZXQ=")
    }

    "changeme equals to YWRtaW5BcHBTZWNyZXQ=" >> {
      BaseEncoding.base64().encode("changeme".getBytes) must beEqualTo("Y2hhbmdlbWU=")
    }
  }

}
