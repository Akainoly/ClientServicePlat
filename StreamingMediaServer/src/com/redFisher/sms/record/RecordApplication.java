package com.redFisher.sms.record;

import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.stream.IBroadcastStream;

public class RecordApplication extends MultiThreadedApplicationAdapter {

	@Override
	public boolean appConnect(IConnection arg0, Object[] arg1) {
		return super.appConnect(arg0, arg1);
	}

	@Override
	public void appDisconnect(IConnection arg0) {
		super.appDisconnect(arg0);
	}

	@Override
	public boolean connect(IConnection arg0, IScope arg1, Object[] arg2) {
		return super.connect(arg0, arg1, arg2);
	}

	@Override
	public void disconnect(IConnection arg0, IScope arg1) {
		super.disconnect(arg0, arg1);
	}

	@Override
	public void streamBroadcastClose(IBroadcastStream arg0) {
		super.streamBroadcastClose(arg0);
	}

	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		super.streamBroadcastStart(stream);
	}

	@Override
	public void streamPublishStart(IBroadcastStream stream) {
		super.streamPublishStart(stream);
	}

}
