// package dev.myclinic.scala.webclient

// import endpoints4s.xhr
// import dev.myclinic.scala.api.ApiEndpoints
// import scala.concurrent.ExecutionContext.Implicits.global
// import endpoints4s.Valid
// import endpoints4s.Invalid

// object implicits {
//   implicit val executionContext = global
// }

// object Api
//     extends ApiEndpoints
//     with xhr.future.Endpoints
//     with xhr.JsonEntitiesFromSchemas {

//   def fromJson[T](json: String)(implicit schema: JsonSchema[T]): T = {
//     stringCodec[T].decode(json) match {
//       case Valid(value) => value
//       case Invalid(errors) => {
//         println(errors);
//         throw new RuntimeException("Invalid app event: ${json}")
//       }
//     }
//   }

//   def toJson[T](value: T)(implicit schema: JsonSchema[T]): String = {
//     schema.encoder.encode(value).render()
//   }
// }

