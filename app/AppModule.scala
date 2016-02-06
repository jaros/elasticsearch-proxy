import com.google.inject.AbstractModule

class AppModule extends AbstractModule {
  override def configure(): Unit = {
//    bind(classOf[ElasticsearchService]).asEagerSingleton()
  }
}
