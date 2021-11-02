package tools;

public class Pkt {
	PktHeader pktHeader;
	String data;

	public Pkt(PktHeader pktHeader, String data){
		this.pktHeader= pktHeader;
		this.data = data;
	}

	@Override
        public java.lang.String toString() {
                return super.toString();
        }

}
