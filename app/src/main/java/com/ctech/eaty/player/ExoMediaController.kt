package com.ctech.eaty.player

import android.app.Activity
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.View
import com.ctech.eaty.util.EventLogger
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import io.reactivex.Completable

class ExoMediaController(private val context: Activity) : MediaController<SimpleExoPlayerView> {

    private var player: SimpleExoPlayer
    private val bandwidthMeter = DefaultBandwidthMeter()
    private val mediaDataSourceFactory: DataSource.Factory = buildDataSourceFactory(bandwidthMeter)
    private val mainHandler: Handler
    private val eventLogger: EventLogger
    private var currentTrackUri = Uri.EMPTY

    init {
        val trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(trackSelectionFactory)
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        mainHandler = Handler()
        eventLogger = EventLogger(trackSelector)
    }

    override fun play(uri: Uri): Completable {
        return Completable.fromAction {
            if (currentTrackUri == uri) {
                player.playWhenReady = !player.playWhenReady
            } else {
                val source = buildMediaSource(uri)
                player.run {
                    stop()
                    playWhenReady = true
                    prepare(source)
                }
            }
            currentTrackUri = uri
        }
    }

    override fun resume(): Completable {
        return Completable.fromAction {
            player.playWhenReady = true
        }
    }

    override fun pause(): Completable {
        return Completable.fromAction {
            player.playWhenReady = false
        }
    }

    override fun release() {
        player.release()
    }

    override fun takeView(view: SimpleExoPlayerView) {

    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val type = Util.inferContentType(uri)
        return when (type) {
            C.TYPE_SS -> SsMediaSource(uri, buildDataSourceFactory(bandwidthMeter),
                    DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger)

            C.TYPE_DASH -> DashMediaSource(uri, buildDataSourceFactory(bandwidthMeter),
                    DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger)

            C.TYPE_HLS -> HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger)

            C.TYPE_OTHER -> ExtractorMediaSource(uri, mediaDataSourceFactory, DefaultExtractorsFactory(), mainHandler, eventLogger)

            else -> {
                throw IllegalStateException("Unsupported type: " + type)
            }
        }
    }

    private fun buildDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter): DataSource.Factory {
        return DefaultDataSourceFactory(context, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter))
    }

    private fun buildHttpDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(Util.getUserAgent(context, "EatyRadio"), bandwidthMeter)
    }
}