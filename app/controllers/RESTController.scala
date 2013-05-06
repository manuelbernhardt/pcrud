package controllers

import play.api.mvc._
import play.api.libs.json._

/**
 * Prototyping a REST controller
 *
 * Goals:
 * - work out-of-the-box without needing to implement anything
 * - support the basic operations
 * - support for content-negotiation
 * - provide a simple UI (following POST-REDIRECT-GET)
 * - be flexible enough to allow for customized implementations with different storage mechanisms
 * - it should be possible to refine functionality by overriding base methods
 *
 * Non-goals:
 * - supporting advanced authentication / authorization constraints
 *
 * Questions:
 * - for the HTML views, can we find a simple way to provide context for custom implementations?
 *   (e.g. for the language)
 * - do we want to support some kind of pagination for the list method?
 * - what about validation?
 *
 *
 * @tparam ResourceType the type of the resource, typically a case-class
 * @tparam IdType the type of the identifier, as it may differ from one storage mechanism to the other
 */
abstract class RESTController[ResourceType <: AnyRef, IdType <: Any] extends Controller {

  implicit val resourceWrites = Json.writes[ResourceType]

  val storage: RESTStorage[ResourceType, IdType]

  /**
   * Lists all resources
   */
  def list() = Action {
    implicit request =>
      val resources = storage.findAll()
      render {
        case Accepts.Html =>
          // here we could provide a default scala template to be used
          // or, if we happen to find a template that matches a given naming convention, use that one instead
          Ok()
        case Accepts.Json =>
          // this would only work for resources that are case classes or can otherwise be dealt with by the macro json support
          Ok(Json.toJson(resources))
      }
  }

  def get(id: AnyRef) = TODO

  def post() = TODO

  def put(id: AnyRef) = TODO

  def delete(id: AnyRef) = TODO

}

/**
 * Simple storage abstraction.
 * This should just do basic things such as listing, retrieving based on an id, creating/updating & deleting
 *
 *
 * @tparam ResourceType the type of the resource to store
 * @tparam IdType the type of the ID
 */
abstract class RESTStorage[ResourceType <: AnyRef, IdType <: Any] {

  def findAll(): List[ResourceType]

  def findOne(id: IdType): Option[ResourceType]

  def save(resource: ResourceType)

  def update(id: IdType, resource: ResourceType)

  def delete(id: IdType)

}
