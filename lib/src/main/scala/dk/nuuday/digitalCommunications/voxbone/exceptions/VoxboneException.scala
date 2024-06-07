package dk.nuuday.digitalCommunications.voxbone.exceptions

sealed abstract class VoxboneExceptionTrait(val msg: String) extends Exception(msg)
case class VoxboneException(override val msg: String) extends VoxboneExceptionTrait(msg)
