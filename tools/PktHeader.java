package tools;

public class PktHeader {

	int ts_sec;         /* timestamp seconds */
        int ts_usec;        /* timestamp microseconds */
        int incl_len;       /* number of octets of packet saved in file */
        int orig_len;       /* actual length of packet */

        public PktHeader(int ts_sec,int ts_usec,int incl_len,int orig_len){
                this.ts_sec= ts_sec;
                this.ts_usec = ts_usec;
                this.incl_len = incl_len;
                this.orig_len = orig_len;
        }

        @Override
        public java.lang.String toString() {
                return super.toString();
        }
}
