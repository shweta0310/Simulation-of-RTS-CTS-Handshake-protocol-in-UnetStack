import org.arl.unet.*
import org.arl.unet.phy.*
import org.arl.unet.*
import org.arl.unet.phy.*
import org.arl.unet.mac.*
//import org.arl.unet.nodeinfo.NodeInfo
//import org.arl.unet.PDU 
import org.arl.fjage.*
//import static org.arl.unet.Services.*
//import static org.arl.unet.phy.Physical.*

subscribe phy

// add a closure to define the 'ping' command
send = { addr, value ->
def mac=agentForService Services.MAC

def req = new ReservationReq(recipient: mac, to: addr, duration: 5.second)
  println req
  def rsp = request req
  println rsp
  if (rsp && rsp.performative == Performative.AGREE) {
    // wait for a channel reservation notification
	println "AGREE"
    def ntf = receive(ReservationStatusNtf, 5000)
	println ntf
    if (ntf && ntf.requestID == req.msgID && ntf.status == ReservationStatus.START) {
      // transmit data for requested duration
      //  :
       def rxNtf1 = receive({ it instanceof RxFrameNtf && it.from == addr}, 5000)
       println rxNtf1
       println "Received CTS"
      println "sending $value to node $addr"
	phy << new DatagramReq(to: addr, protocol: Protocol.DATA, data: value)
  def txNtf = receive(TxFrameNtf, 1000)
  def rxNtf = receive({ it instanceof RxFrameNtf && it.from == addr}, 5000)
    if (txNtf && rxNtf && rxNtf.from == addr)
      println "Data Received at ${rxNtf.to} from ${rxNtf.from} is: ${rxNtf.data}"  
	/*def txNtf = receive(TxFrameNtf, 1000)
  	println txNtf
  	def rxNtf = receive({ it instanceof RxFrameNtf && it.from == addr}, 5000)
  	println rxNtf
    	if (txNtf && rxNtf && rxNtf.from == addr)
      		println "Data Received at ${rxNtf.to} from ${rxNtf.from} is: ${rxNtf.data}"  
	}*/
	}
	ntf =receive(ReservationStatusNtf, 7000)
	println ntf
	if (ntf && ntf.requestID == req.msgID && ntf.status == ReservationStatus.END) {
		println "xyz"
	}
  }
}
