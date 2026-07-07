# Offline Cache Plan

The Android app should treat offline support as a first-class feature for read-heavy study-planning content.

## What can work offline

- cached programs
- cached universities
- cached scholarships
- cached guides and articles
- cached countries
- saved items
- recently viewed content
- cached Fit Finder results
- cached chat history or message previews
- small app settings stored in DataStore

## What should require internet

- fresh search results
- new program detail fetches that are not cached yet
- auth/session refresh
- saving and unsaving items when the local queue has not synced yet
- Fit Finder submissions that need server scoring or AI explanation
- AI chat requests
- reporting content
- any action that depends on current server permissions

## Cache strategy

Use Room for structured offline cache and DataStore for small settings or session metadata.

Suggested cache groups:

- high-value public content with moderate TTL
- saved items with local-first read behavior
- recently viewed items with a local cap
- Fit Finder results with a result-specific cache entry
- chat transcripts with a small local message window
- sync queue entries for offline write actions

## Freshness ideas

These are planning values, not final policy:

- programs, universities, scholarships, and guides: 24 hours to 7 days depending on volatility
- countries: 7 days to 30 days
- saved items: sync immediately when online
- recently viewed: keep locally until user clears or a cap is reached
- Fit Finder results: keep until replaced or manually cleared
- chat messages: keep recent conversation history locally, but sync server copy when online

## Sync queue

Use a local queue for offline user actions such as:

- save item
- remove saved item
- submit report
- refresh profile data
- retry failed content refreshes

Queue rules:

- keep actions idempotent when possible
- store only safe metadata
- replay actions when connectivity returns
- mark actions failed only after a real server rejection

## Offline user warning

When cached content is shown, the app should display this warning text or a close variant:

> You are offline. Showing saved or recently viewed information. Details such as deadlines, tuition, and scholarship rules may have changed. Confirm on the official source before applying.

## UX behavior

- show cached content immediately when available
- show a clear stale-data banner when offline or when the cache is older than the freshness threshold
- allow reading offline even if some detail fields are missing
- avoid pretending cached data is fresh when the app cannot confirm it
- surface retry actions for write operations that are waiting in the sync queue

## Caching priorities

Highest priority:

- saved items
- recently viewed items
- Fit Finder results

Medium priority:

- programs
- universities
- scholarships
- guides
- countries

Lower priority:

- non-essential metadata and decorative content