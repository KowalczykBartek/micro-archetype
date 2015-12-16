package com.micro.archetype.webclient;

import java.util.function.Supplier;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * HystrixWebWrapper is abstraction over all calls to external dependencies
 * for more information go to {@see https://github.com/Netflix/Hystrix}
 */
public class HystrixWebWrapper<T> extends HystrixCommand<T>
{
	private final Supplier<T> call;

	public HystrixWebWrapper(final String commandName, Supplier<T> call)
	{
		super(HystrixCommandGroupKey.Factory.asKey(commandName));
		this.call = call;
	}

	@Override
	protected T run() throws Exception
	{
		return call.get();
	}
}
