package videoPanel;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 * MediaPlayerEventListenerAdapter is implements MediaPlayerEventListener, which allows only some
 * methods to be overwritten. This saves space whenever a full listener would have been used.
 */
public class MediaPlayerEventListenerAdapter implements
		MediaPlayerEventListener {

	@Override
	public void backward(MediaPlayer arg0) {

	}

	@Override
	public void buffering(MediaPlayer arg0, float arg1) {

	}

	@Override
	public void endOfSubItems(MediaPlayer arg0) {

	}

	@Override
	public void error(MediaPlayer arg0) {

	}

	@Override
	public void finished(MediaPlayer arg0) {

	}

	@Override
	public void forward(MediaPlayer arg0) {

	}

	@Override
	public void lengthChanged(MediaPlayer arg0, long arg1) {

	}

	@Override
	public void mediaChanged(MediaPlayer arg0, libvlc_media_t arg1, String arg2) {

	}

	@Override
	public void mediaDurationChanged(MediaPlayer arg0, long arg1) {

	}

	@Override
	public void mediaFreed(MediaPlayer arg0) {

	}

	@Override
	public void mediaMetaChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void mediaParsedChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void mediaStateChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {

	}

	@Override
	public void newMedia(MediaPlayer arg0) {

	}

	@Override
	public void opening(MediaPlayer arg0) {

	}

	@Override
	public void pausableChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void paused(MediaPlayer arg0) {

	}

	@Override
	public void playing(MediaPlayer arg0) {

	}

	@Override
	public void positionChanged(MediaPlayer arg0, float arg1) {

	}

	@Override
	public void seekableChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void snapshotTaken(MediaPlayer arg0, String arg1) {

	}

	@Override
	public void stopped(MediaPlayer arg0) {

	}

	@Override
	public void subItemFinished(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void subItemPlayed(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void timeChanged(MediaPlayer arg0, long arg1) {

	}

	@Override
	public void titleChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void videoOutput(MediaPlayer arg0, int arg1) {

	}
}