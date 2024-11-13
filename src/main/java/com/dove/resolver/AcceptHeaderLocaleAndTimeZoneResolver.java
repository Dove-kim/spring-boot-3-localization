package com.dove.resolver;


import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

public class AcceptHeaderLocaleAndTimeZoneResolver extends AbstractLocaleContextResolver {
    public static final String LOCALE_HEADER_ATTRIBUTE_NAME = "Accept-Language";
    public static final String TIME_ZONE_HEADER_ATTRIBUTE_NAME = "Accept-TimeZone";


    @Override
    public LocaleContext resolveLocaleContext(final HttpServletRequest request) {
        Locale locale = resolveLocale(request);
        TimeZone timeZone = resolveTimeZone(request);

        return new TimeZoneAwareLocaleContext() {
            @Override
            @Nullable
            public Locale getLocale() {
                return locale;
            }

            @Override
            @Nullable
            public TimeZone getTimeZone() {
                return timeZone;
            }
        };
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale defaultLocale = getDefaultLocale();
        String acceptLanguage = request.getHeader(LOCALE_HEADER_ATTRIBUTE_NAME);

        if (StringUtils.hasText(acceptLanguage)) {
            return Locale.forLanguageTag(acceptLanguage);
        } else {
            return (defaultLocale != null) ? defaultLocale : request.getLocale();
        }
    }

    private TimeZone resolveTimeZone(HttpServletRequest request) {
        String timeZoneHeader = request.getHeader(TIME_ZONE_HEADER_ATTRIBUTE_NAME);

        if (StringUtils.hasText(timeZoneHeader)) {
            try {
                ZoneId zoneId = ZoneId.of(timeZoneHeader);
                return TimeZone.getTimeZone(zoneId);
            } catch (DateTimeException e) {
                // 유효하지 않은 timeZoneHeader 값을 무시하고 기본 TimeZone을 설정
                return getDefaultTimeZone() != null ? getDefaultTimeZone() : TimeZone.getDefault();
            }
        } else {
            return getDefaultTimeZone() != null ? getDefaultTimeZone() : TimeZone.getDefault();
        }
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable LocaleContext localeContext) {
        throw new UnsupportedOperationException("Cannot change HTTP headers - use a different locale resolution strategy");
    }

    @Override
    public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
        throw new UnsupportedOperationException("Cannot change HTTP Accept-Language header - use a different locale resolution strategy");
    }


}
