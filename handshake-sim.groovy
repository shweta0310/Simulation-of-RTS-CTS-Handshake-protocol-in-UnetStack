//! Simulation: 2-node network with ping daemons

import org.arl.fjage.Message
import org.arl.unet.*
import org.arl.mac.*
import org.arl.unet.phy.*

import org.arl.fjage.RealTimePlatform

println '''
2-node network with handshake technique
-----------------------------------------
Node 1 will send a RTS to node 2
The agent MyHandshakeMac present at node 2, will check the state of msg recieved, as it should be RTS, then it will send CTS to the sender node address and reserve the channel till timeout period(5sec)
Sender node will recieve CTS and then it will send value to node 2.

You can interact with node 1 in the console shell. For example, try:
> send <to-address> , <data>
For example:
> send 2, 7

When you are done, exit the shell by pressing ^D or entering:
> shutdown
'''

platform = RealTimePlatform

// run simulation forever
simulate {
  node '1', address: 1, remote: 1103, location: [0, 0, 0], shell: true, stack: { container ->
    container.add 'hand', new MySimpleHandshakeMac()
    container.shell.addInitrc "${script.parent}/fshrc2.groovy"
  }
  node '2', address: 2, remote: 1104, location: [1.km, 0, 0], shell:5103, stack: { container ->
    container.add 'hand', new MySimpleHandshakeMac()
  }
}
