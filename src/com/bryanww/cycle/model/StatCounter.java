package com.bryanww.cycle.model;

/**
 * Simple value aggregator that tracks basic statistics, such as count, sum,
 * last, max, min, and average by passing values to the
 * {@link #addValue(double)} method.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class StatCounter {
	
	protected long   count;  // the total number of values added
	protected double last;   // the last value added
	protected double sum;    // the sum of all values added
	protected double max;    // the maximum value of all values added
	protected double min;    // the minimum value of all values added
	
	/**
	 * Creates a new {@link StatCounter}.
	 */
	public StatCounter() {
		reset();
	}
	
	/**
	 * Resets the values tracked by this statistics counter.
	 */
	public void reset() {
		this.count = 0L;
		this.last  = Double.NaN;
		this.sum   = 0.0;
		this.max   = Double.NaN;
		this.min   = Double.NaN;
	}
	
	/**
	 * Adds an arbitrary value to the statistics counter, incrementing totals and setting max, min, etc.
	 * @param value the arbitrary value to add
	 */
	public void addValue(double value) {
		count++;
		last  = value;
		sum  += value;
		if (Double.isNaN(max) || value > max) {
			max = value;
		}
		if (Double.isNaN(min) || value < min) {
			min = value;
		}
	}
	
	/**
	 * Returns the total number of values added.
	 * @return the total number of values added
	 */
	public long getCount() {
		return count;
	}
	
	/**
	 * Returns the last value added.
	 * @return the last value added
	 */
	public double getLast() {
		return last;
	}
	
	/**
	 * Returns the sum of all values added.
	 * @return the sum of all values added
	 */
	public double getSum() {
		return sum;
	}
	
	/**
	 * Returns the maximum value of all values added.
	 * @return the maximum value of all values added
	 */
	public double getMax() {
		return max;
	}
	
	/**
	 * Returns the minimum value of all values added.
	 * @return the minimum value of all values added
	 */
	public double getMin() {
		return min;
	}
	
	/**
	 * Returns the average value, which is the sum value divided by the count.  Returns {@link Double#NaN} if not set.
	 * @return the average value
	 */
	public double getAverage() {
		return count == 0L ? Double.NaN : (sum / (double) count);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(getClass().getName()).append(':');
		
		builder.append("count=").append(count);
		builder.append("|last=").append(last);
		builder.append("|sum=").append(sum);
		builder.append("|max=").append(max);
		builder.append("|min=").append(min);
		builder.append("|average=").append(getAverage());
		
		return builder.append(']').toString();
	}
}
