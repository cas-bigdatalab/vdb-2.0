package vdb.mydb.rpc.service;

import java.io.Serializable;
import java.util.concurrent.Callable;

public interface RPCCallable<T> extends Callable<T>, Serializable
{

}
