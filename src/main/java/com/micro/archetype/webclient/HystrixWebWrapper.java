package com.micro.archetype.webclient;

import java.util.Optional;
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
	private final Optional<Supplier<T>> optionalFallback;

	public HystrixWebWrapper(final String commandName, Supplier<T> call)
	{
		super(HystrixCommandGroupKey.Factory.asKey(commandName));
		this.call = call;
		this.optionalFallback = Optional.empty();
	}

	public HystrixWebWrapper(final String commandName, Supplier<T> call, Supplier<T> fallback)
	{
		super(HystrixCommandGroupKey.Factory.asKey(commandName));
		this.call = call;
		this.optionalFallback = Optional.of(fallback);
	}

	@Override
	protected T run() throws Exception
	{
		return call.get();
	}

	@Override
	protected T getFallback()
	{
		//FIXME add logging here
		return optionalFallback//
				.map(fallback -> fallback.get())//
				.orElse(null);
	}
}
