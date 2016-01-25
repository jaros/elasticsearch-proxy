package service

import org.elasticsearch.ElasticsearchParseException
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.{IndicesAdminClient, Client}
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder
import org.elasticsearch.node.NodeBuilder.nodeBuilder


class IndexManager {


  case class FieldAttributes(name: String, `type`: String, boost: Double, analyzer: String)

  case class ElasticSearchIndex(id: Int,
                                name: String,

                                ownerId: String,
                                defaultFieldType: String = "not_analyzed", // required
                                timestamps: Boolean = true, // optional
                                keepHistory: Boolean = true, // for further implementation
                                autocomplete: Boolean = true, // for further implementation
                                fieldsAttributes: List[FieldAttributes]
                               )


  lazy val esClient: Client = nodeBuilder.node.client

  def createIndex(indexName: String, indexType: String, index: ElasticSearchIndex) =  {
     val mapping : XContentBuilder = buildFieldsMapping(index)
    //var created: Boolean
    if (indicesAdmin.prepareExists(indexName).get().isExists) {
      indicesAdmin.preparePutMapping(indexName).setType(indexType).setSource(mapping).get().isAcknowledged
    } else {
     indicesAdmin.prepareCreate(indexName).addMapping(indexType, mapping).get().isAcknowledged
    }
    //return created ? Status.OK : Status.NOK("index was not created");
  }//esClient.prepareIndex(index.name)

  def indicesAdmin: IndicesAdminClient = esClient.admin.indices

  def isFieldIndexed(esIndex: ElasticSearchIndex) = if ("ignored".equals(esIndex.defaultFieldType)) "no" else esIndex.defaultFieldType


  def buildFieldsMapping(esIndex: ElasticSearchIndex): XContentBuilder = {
    if (esIndex == null) {
      throw new ElasticsearchParseException("provided index data cannot be parsed")
    }
    val builder: XContentBuilder = jsonBuilder().startObject().startObject("properties")

    esIndex.fieldsAttributes.foreach(field =>
      builder.startObject(field.name)
        .field("type", field.`type`)
        .field("boost", field.boost)
        .field("analyzer", field.analyzer)
        .field("index", isFieldIndexed(esIndex)).endObject()
    )

    builder.endObject.endObject
  }

}
