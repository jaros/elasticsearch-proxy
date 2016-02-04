package service

import javax.inject._

import org.elasticsearch.common.settings.Settings.builder
import org.elasticsearch.node.Node
import org.elasticsearch.node.NodeBuilder.nodeBuilder
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

class ElasticsearchService @Inject()(lifecycle: ApplicationLifecycle) {

  val node: Node = nodeBuilder()
    .settings(builder()
      .put("path.home", "./es_node")
    )
    .data(false).client(true).node()

  val client = node.client()

  lifecycle.addStopHook { () =>
    Future.successful(node.close())
  }
}
