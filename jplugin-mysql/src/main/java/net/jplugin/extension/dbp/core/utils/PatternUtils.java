package net.jplugin.extension.dbp.core.utils;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @Author: yuanhongjun
 * DateTime: 2022-01-06 16:31
 */
public class PatternUtils {

    public static Pattern SHOW_PATTERN = Pattern.compile("((?i)show)(\\s+)(\\w+)(\\s*)");
    public static Pattern SHOW_TABLES_PATTERN = Pattern.compile("((?i)show)(\\s+)(((?i)full)(\\s+))?((?i)tables)((\\s+)(\\w+)(\\s*))?");
    public static Pattern DESC_TABLE_PATTERN = Pattern.compile("((?i)show)(\\s+)((?i)table)(\\s+)((?i)status)(\\s+)((?i)like)(\\s+)\\'(\\w+)\\'(\\s*)");
    public static Pattern SETTINGS_PATTERN = Pattern.compile("((?i)set)(\\s+)(\\w+)(\\s*)(=*)(\\s*)(\\w+)");
    public static Pattern SELECT_SETTINGS_PATTERN = Pattern.compile("@@((\\w+)((\\.)(\\w+))?)\\s*(((?i)AS)\\s(\\w+))?");
    public static Pattern SHOW_CREATE_TABLE_PATTERN = Pattern.compile("((?i)show)(\\s+)((?i)create)(\\s+)((?i)table)(\\s+)`(\\w+)`(\\s*)");
}
