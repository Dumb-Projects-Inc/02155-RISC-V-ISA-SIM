package rvsim

object Types {
  type INT_8 = Byte
  type INT_16 = Short
  type INT_32 = Int
  type INT_64 = Long

  // Anyval class to represent unsigned 32-bit integers, since Scala does not have native unsigned types
  final class UINT_32(val value: Long) extends AnyVal {
    @inline def raw: Long = value & 0xffffffffL

    @inline def +(that: UINT_32): UINT_32 =
      new UINT_32((this.raw + that.raw) & 0xffffffffL)
    @inline def -(that: UINT_32): UINT_32 =
      new UINT_32((this.raw - that.raw) & 0xffffffffL)

    @inline def ==(that: UINT_32): Boolean = this.raw == that.raw
    @inline def !=(that: UINT_32): Boolean = this.raw != that.raw

    @inline private def toMaskLong(x: Any): Long = x match {
      case u: UINT_32 => u.raw
      case i: Int     => i.toLong & 0xffffffffL
      case l: Long    => l & 0xffffffffL
      case _ =>
        throw new IllegalArgumentException(
          f"Cannot mask type ${x.getClass} to UINT_32"
        )
    }

    @inline private def cmp[T](that: T)(f: (Long, Long) => Boolean): Boolean =
      f(this.raw, toMaskLong(that))
    @inline private def arr[T](that: T)(f: (Long, Long) => Long): UINT_32 =
      new UINT_32(f(this.raw, toMaskLong(that)) & 0xffffffffL)
    @inline def ==[T](that: T): Boolean = cmp(that)(_ == _)
    @inline def !=[T](that: T): Boolean = cmp(that)(_ != _)
    @inline def <[T](that: T): Boolean = cmp(that)(_ < _)
    @inline def >[T](that: T): Boolean = cmp(that)(_ > _)
    @inline def <=[T](that: T): Boolean = cmp(that)(_ <= _)
    @inline def >=[T](that: T): Boolean = cmp(that)(_ >= _)
    @inline def %[T](that: T): UINT_32 = arr(that)(_ % _)
    @inline def &[T](that: T): UINT_32 = arr(that)(_ & _)
    @inline def |[T](that: T): UINT_32 = arr(that)(_ | _)
    @inline def ^[T](that: T): UINT_32 = arr(that)(_ ^ _)
    @inline def <<[T](that: T): UINT_32 = arr(that)((a, b) => a << b)
    @inline def >>[T](that: T): UINT_32 = arr(that)((a, b) => a >>> b)

    @inline def toInt(): Int = (this.raw & 0xffffffffL).toInt
    @inline def toLong(): Long = this.raw
    @inline def toByte(): Byte = (this.raw & 0xffL).toByte
    @inline def toHexString: String = f"${this.raw}%08x"
    override def toString: String = f"0x${raw}%08x"

  }
  object UINT_32 {
    @inline def apply(x: Long): UINT_32 = new UINT_32(x & 0xffffffffL)
    @inline def fromInt(x: Int): UINT_32 = new UINT_32(x.toLong & 0xffffffffL)
    @inline def fromLong(x: Long): UINT_32 = new UINT_32(x & 0xffffffffL)

    val zero: UINT_32 = new UINT_32(0L)
  }

  implicit class UInt32SyntaxInt(val x: Int) extends AnyVal {
    @inline def u32: UINT_32 = new UINT_32(x.toLong & 0xffffffffL)
  }

  implicit class UInt32SyntaxLong(val x: Long) extends AnyVal {
    @inline def u32: UINT_32 = new UINT_32(x & 0xffffffffL)
  }

}
