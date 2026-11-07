package com.nexusai.workspace.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nexusai.workspace.data.local.entity.UserPreferenceEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserPreferencesDao_Impl implements UserPreferencesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserPreferenceEntity> __insertionAdapterOfUserPreferenceEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateApiKey;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePromptQuota;

  public UserPreferencesDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserPreferenceEntity = new EntityInsertionAdapter<UserPreferenceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_preferences` (`id`,`hasCompletedOnboarding`,`userName`,`userRole`,`primaryGoal`,`geminiApiKey`,`dailyPromptCount`,`maxDailyPrompts`,`lastPromptResetDate`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserPreferenceEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.getHasCompletedOnboarding() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindString(3, entity.getUserName());
        statement.bindString(4, entity.getUserRole());
        statement.bindString(5, entity.getPrimaryGoal());
        statement.bindString(6, entity.getGeminiApiKey());
        statement.bindLong(7, entity.getDailyPromptCount());
        statement.bindLong(8, entity.getMaxDailyPrompts());
        statement.bindString(9, entity.getLastPromptResetDate());
      }
    };
    this.__preparedStmtOfUpdateApiKey = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_preferences SET geminiApiKey = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePromptQuota = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_preferences SET dailyPromptCount = ?, lastPromptResetDate = ? WHERE id = 1";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrUpdate(final UserPreferenceEntity preferences,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserPreferenceEntity.insert(preferences);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateApiKey(final String apiKey, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateApiKey.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, apiKey);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateApiKey.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePromptQuota(final int count, final String resetDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePromptQuota.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, count);
        _argIndex = 2;
        _stmt.bindString(_argIndex, resetDate);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdatePromptQuota.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserPreferenceEntity> getUserPreferencesFlow() {
    final String _sql = "SELECT * FROM user_preferences WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_preferences"}, new Callable<UserPreferenceEntity>() {
      @Override
      @Nullable
      public UserPreferenceEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHasCompletedOnboarding = CursorUtil.getColumnIndexOrThrow(_cursor, "hasCompletedOnboarding");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfUserRole = CursorUtil.getColumnIndexOrThrow(_cursor, "userRole");
          final int _cursorIndexOfPrimaryGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "primaryGoal");
          final int _cursorIndexOfGeminiApiKey = CursorUtil.getColumnIndexOrThrow(_cursor, "geminiApiKey");
          final int _cursorIndexOfDailyPromptCount = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyPromptCount");
          final int _cursorIndexOfMaxDailyPrompts = CursorUtil.getColumnIndexOrThrow(_cursor, "maxDailyPrompts");
          final int _cursorIndexOfLastPromptResetDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPromptResetDate");
          final UserPreferenceEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpHasCompletedOnboarding;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfHasCompletedOnboarding);
            _tmpHasCompletedOnboarding = _tmp != 0;
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpUserRole;
            _tmpUserRole = _cursor.getString(_cursorIndexOfUserRole);
            final String _tmpPrimaryGoal;
            _tmpPrimaryGoal = _cursor.getString(_cursorIndexOfPrimaryGoal);
            final String _tmpGeminiApiKey;
            _tmpGeminiApiKey = _cursor.getString(_cursorIndexOfGeminiApiKey);
            final int _tmpDailyPromptCount;
            _tmpDailyPromptCount = _cursor.getInt(_cursorIndexOfDailyPromptCount);
            final int _tmpMaxDailyPrompts;
            _tmpMaxDailyPrompts = _cursor.getInt(_cursorIndexOfMaxDailyPrompts);
            final String _tmpLastPromptResetDate;
            _tmpLastPromptResetDate = _cursor.getString(_cursorIndexOfLastPromptResetDate);
            _result = new UserPreferenceEntity(_tmpId,_tmpHasCompletedOnboarding,_tmpUserName,_tmpUserRole,_tmpPrimaryGoal,_tmpGeminiApiKey,_tmpDailyPromptCount,_tmpMaxDailyPrompts,_tmpLastPromptResetDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUserPreferences(final Continuation<? super UserPreferenceEntity> $completion) {
    final String _sql = "SELECT * FROM user_preferences WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserPreferenceEntity>() {
      @Override
      @Nullable
      public UserPreferenceEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHasCompletedOnboarding = CursorUtil.getColumnIndexOrThrow(_cursor, "hasCompletedOnboarding");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfUserRole = CursorUtil.getColumnIndexOrThrow(_cursor, "userRole");
          final int _cursorIndexOfPrimaryGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "primaryGoal");
          final int _cursorIndexOfGeminiApiKey = CursorUtil.getColumnIndexOrThrow(_cursor, "geminiApiKey");
          final int _cursorIndexOfDailyPromptCount = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyPromptCount");
          final int _cursorIndexOfMaxDailyPrompts = CursorUtil.getColumnIndexOrThrow(_cursor, "maxDailyPrompts");
          final int _cursorIndexOfLastPromptResetDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPromptResetDate");
          final UserPreferenceEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpHasCompletedOnboarding;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfHasCompletedOnboarding);
            _tmpHasCompletedOnboarding = _tmp != 0;
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpUserRole;
            _tmpUserRole = _cursor.getString(_cursorIndexOfUserRole);
            final String _tmpPrimaryGoal;
            _tmpPrimaryGoal = _cursor.getString(_cursorIndexOfPrimaryGoal);
            final String _tmpGeminiApiKey;
            _tmpGeminiApiKey = _cursor.getString(_cursorIndexOfGeminiApiKey);
            final int _tmpDailyPromptCount;
            _tmpDailyPromptCount = _cursor.getInt(_cursorIndexOfDailyPromptCount);
            final int _tmpMaxDailyPrompts;
            _tmpMaxDailyPrompts = _cursor.getInt(_cursorIndexOfMaxDailyPrompts);
            final String _tmpLastPromptResetDate;
            _tmpLastPromptResetDate = _cursor.getString(_cursorIndexOfLastPromptResetDate);
            _result = new UserPreferenceEntity(_tmpId,_tmpHasCompletedOnboarding,_tmpUserName,_tmpUserRole,_tmpPrimaryGoal,_tmpGeminiApiKey,_tmpDailyPromptCount,_tmpMaxDailyPrompts,_tmpLastPromptResetDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
