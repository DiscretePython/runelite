/*
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
 * Copyright (c) 2018, Ron Young <https://github.com/raiyni>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.bosstimer;

import lombok.AccessLevel;
import lombok.Getter;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class RespawnTimersSession
{
	@Getter(AccessLevel.PACKAGE)
	private Map<String, ArrayList<RespawnTimer>> timersMap = new HashMap<>();

	boolean isEmpty()
	{
		return timersMap.isEmpty();
	}

	void addBossTimer(RespawnTimer respawnTimer)
	{
		timersMap.computeIfAbsent(respawnTimer.getBossName(), k -> new ArrayList<>());

		ArrayList<RespawnTimer> timers = timersMap.get(respawnTimer.getBossName());
		timers.add(respawnTimer);
	}

	void cull()
	{
		Iterator<HashMap.Entry<String, ArrayList<RespawnTimer>>> mapIterator = timersMap.entrySet().iterator();

		while (mapIterator.hasNext())
		{
			final HashMap.Entry<String, ArrayList<RespawnTimer>> entry = mapIterator.next();
			Iterator<RespawnTimer> valueIterator = entry.getValue().iterator();

			while (valueIterator.hasNext())
			{
				final RespawnTimer respawnTimer = valueIterator.next();

				if (respawnTimer.cull())
				{
					valueIterator.remove();

					if (entry.getValue().size() == 0)
					{
						mapIterator.remove();
					}
				}
			}
		}
	}
}
