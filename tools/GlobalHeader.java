package tools;

public class GlobalHeader {
	String magic_number;    /* magic number */
        int version_major=0;   /* major version number */
        int version_minor=0;  /* minor version number */
        int thiszone;       /* GMT to local correction */
        int sigfigs;        /* accuracy of timestamps */
        int snaplen;        /* max length of captured packets, in octets */
        int network;        /* data link type */

        public GlobalHeader(String magic_number,int version_major,int version_minor,int thiszone,int sigfigs,int snaplen,int network){
                this.magic_number= magic_number;
                this.version_major = version_major;
                this.version_minor = version_minor;
                this.thiszone = thiszone;
                this.sigfigs = sigfigs;
                this.snaplen = snaplen;
                this.network = network;
        }

        @Override
        public java.lang.String toString() {
                return super.toString();
        }

}
