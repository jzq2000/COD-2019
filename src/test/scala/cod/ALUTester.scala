package cod

import chisel3._
import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}

object ALUTB extends App {
  iotesters.Driver.execute(args, () => new And(32)) {
    c => new AndTester(c)
  }
  iotesters.Driver.execute(args, () => new Or(32)) {
    c => new OrTester(c)
  }
  iotesters.Driver.execute(args, () => new Xor(32)) {
    c => new XorTester(c)
  }
}

class AndTester(c: And) extends PeekPokeTester(c) {
  poke(c.io.A, 0x12345678)
  poke(c.io.B, 0xFEDCBA90)
  step(1)
  expect(c.io.S, 0x12141210)
}

class OrTester(c: Or) extends PeekPokeTester(c) {
  poke(c.io.A, 0x12345678)
  poke(c.io.B, 0xFEDCBA90)
  step(1)
  expect(c.io.S, 0xFEFCFEF8)
}

class XorTester(c: Xor) extends PeekPokeTester(c) {
  poke(c.io.A, 0x12345678)
  poke(c.io.B, 0xFEDCBA90)
  step(1)
  expect(c.io.S, 0xECE8ECE8)
}
