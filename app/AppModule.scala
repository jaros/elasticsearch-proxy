import com.google.inject.AbstractModule
import service.ElasticsearchService

class AppModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ElasticsearchService]).asEagerSingleton()
  }
}
