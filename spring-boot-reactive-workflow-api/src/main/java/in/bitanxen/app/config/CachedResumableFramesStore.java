package in.bitanxen.app.config;

import io.netty.buffer.ByteBuf;
import io.rsocket.resume.ResumableFramesStore;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CachedResumableFramesStore implements ResumableFramesStore {

    @Override
    public Mono<Void> saveFrames(Flux<ByteBuf> frames) {
        return null;
    }

    @Override
    public void releaseFrames(long remoteImpliedPos) {

    }

    @Override
    public Flux<ByteBuf> resumeStream() {
        return null;
    }

    @Override
    public long framePosition() {
        return 0;
    }

    @Override
    public long frameImpliedPosition() {
        return 0;
    }

    @Override
    public boolean resumableFrameReceived(ByteBuf frame) {
        return false;
    }

    @Override
    public Mono<Void> onClose() {
        return null;
    }

    @Override
    public void dispose() {

    }
}
