package org.videolan.vlc.gui;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 频道实体.
 *
 * @author: archko 5/15/14 :9:23 PM
 */
public class Video implements Serializable, Comparator<Video> {

    public static final long serialVersionUID=-4899452726203839401L;

    public String channel_id;
    public String channel_name;
    public String channel_url;
    /**
     * 时间戳,在记录中有用到.
     */
    public long timestampe;

    @Override
    public int compare(Video lhs, Video rhs) {
        if (lhs.timestampe>rhs.timestampe) {    //时间大的放前面
            return -1;
        } else if (lhs.timestampe<rhs.timestampe) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "LBook{"+
            "channel_id='"+channel_id+'\''+
            ", channel_name='"+channel_name+'\''+
            ", channel_url='"+channel_url+'\''+
            ", timestampe="+timestampe+
            '}';
    }
}
