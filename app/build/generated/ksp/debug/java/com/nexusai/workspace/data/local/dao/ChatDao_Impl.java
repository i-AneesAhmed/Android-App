package com.nexusai.workspace.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nexusai.workspace.data.local.entity.ChatMessageEntity;
import com.nexusai.workspace.data.local.entity.ChatThreadEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ChatDao_Impl implements ChatDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChatThreadEntity> __insertionAdapterOfChatThreadEntity;

  private final EntityInsertionAdapter<ChatMessageEntity> __insertionAdapterOfChatMessageEntity;

  private final EntityDeletionOrUpdateAdapter<ChatThreadEntity> __updateAdapterOfChatThreadEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteThread;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessagesForThread;

  public ChatDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChatThreadEntity = new EntityInsertionAdapter<ChatThreadEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chat_threads` (`id`,`title`,`createdTimestamp`,`updatedTimestamp`,`boundDocumentId`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChatThreadEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindLong(3, entity.getCreatedTimestamp());
        statement.bindLong(4, entity.getUpdatedTimestamp());
        if (entity.getBoundDocumentId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getBoundDocumentId());
        }
      }
    };
    this.__insertionAdapterOfChatMessageEntity = new EntityInsertionAdapter<ChatMessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chat_messages` (`id`,`threadId`,`sender`,`content`,`timestamp`,`suggestedActionJson`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChatMessageEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getThreadId());
        statement.bindString(3, entity.getSender());
        statement.bindString(4, entity.getContent());
        statement.bindLong(5, entity.getTimestamp());
        if (entity.getSuggestedActionJson() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getSuggestedActionJson());
        }
      }
    };
    this.__updateAdapterOfChatThreadEntity = new EntityDeletionOrUpdateAdapter<ChatThreadEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `chat_threads` SET `id` = ?,`title` = ?,`createdTimestamp` = ?,`updatedTimestamp` = ?,`boundDocumentId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChatThreadEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindLong(3, entity.getCreatedTimestamp());
        statement.bindLong(4, entity.getUpdatedTimestamp());
        if (entity.getBoundDocumentId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getBoundDocumentId());
        }
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteThread = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM chat_threads WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMessagesForThread = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM chat_messages WHERE threadId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertThread(final ChatThreadEntity thread,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfChatThreadEntity.insertAndReturnId(thread);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMessage(final ChatMessageEntity message,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfChatMessageEntity.insertAndReturnId(message);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateThread(final ChatThreadEntity thread,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChatThreadEntity.handle(thread);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteThread(final long threadId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteThread.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, threadId);
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
          __preparedStmtOfDeleteThread.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMessagesForThread(final long threadId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessagesForThread.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, threadId);
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
          __preparedStmtOfDeleteMessagesForThread.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ChatThreadEntity>> getAllThreadsFlow() {
    final String _sql = "SELECT * FROM chat_threads ORDER BY updatedTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chat_threads"}, new Callable<List<ChatThreadEntity>>() {
      @Override
      @NonNull
      public List<ChatThreadEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCreatedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTimestamp");
          final int _cursorIndexOfUpdatedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedTimestamp");
          final int _cursorIndexOfBoundDocumentId = CursorUtil.getColumnIndexOrThrow(_cursor, "boundDocumentId");
          final List<ChatThreadEntity> _result = new ArrayList<ChatThreadEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChatThreadEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final long _tmpCreatedTimestamp;
            _tmpCreatedTimestamp = _cursor.getLong(_cursorIndexOfCreatedTimestamp);
            final long _tmpUpdatedTimestamp;
            _tmpUpdatedTimestamp = _cursor.getLong(_cursorIndexOfUpdatedTimestamp);
            final Long _tmpBoundDocumentId;
            if (_cursor.isNull(_cursorIndexOfBoundDocumentId)) {
              _tmpBoundDocumentId = null;
            } else {
              _tmpBoundDocumentId = _cursor.getLong(_cursorIndexOfBoundDocumentId);
            }
            _item = new ChatThreadEntity(_tmpId,_tmpTitle,_tmpCreatedTimestamp,_tmpUpdatedTimestamp,_tmpBoundDocumentId);
            _result.add(_item);
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
  public Object getThreadById(final long threadId,
      final Continuation<? super ChatThreadEntity> $completion) {
    final String _sql = "SELECT * FROM chat_threads WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, threadId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ChatThreadEntity>() {
      @Override
      @Nullable
      public ChatThreadEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCreatedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTimestamp");
          final int _cursorIndexOfUpdatedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedTimestamp");
          final int _cursorIndexOfBoundDocumentId = CursorUtil.getColumnIndexOrThrow(_cursor, "boundDocumentId");
          final ChatThreadEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final long _tmpCreatedTimestamp;
            _tmpCreatedTimestamp = _cursor.getLong(_cursorIndexOfCreatedTimestamp);
            final long _tmpUpdatedTimestamp;
            _tmpUpdatedTimestamp = _cursor.getLong(_cursorIndexOfUpdatedTimestamp);
            final Long _tmpBoundDocumentId;
            if (_cursor.isNull(_cursorIndexOfBoundDocumentId)) {
              _tmpBoundDocumentId = null;
            } else {
              _tmpBoundDocumentId = _cursor.getLong(_cursorIndexOfBoundDocumentId);
            }
            _result = new ChatThreadEntity(_tmpId,_tmpTitle,_tmpCreatedTimestamp,_tmpUpdatedTimestamp,_tmpBoundDocumentId);
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

  @Override
  public Flow<List<ChatMessageEntity>> getMessagesForThreadFlow(final long threadId) {
    final String _sql = "SELECT * FROM chat_messages WHERE threadId = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, threadId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chat_messages"}, new Callable<List<ChatMessageEntity>>() {
      @Override
      @NonNull
      public List<ChatMessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfThreadId = CursorUtil.getColumnIndexOrThrow(_cursor, "threadId");
          final int _cursorIndexOfSender = CursorUtil.getColumnIndexOrThrow(_cursor, "sender");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSuggestedActionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "suggestedActionJson");
          final List<ChatMessageEntity> _result = new ArrayList<ChatMessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChatMessageEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpThreadId;
            _tmpThreadId = _cursor.getLong(_cursorIndexOfThreadId);
            final String _tmpSender;
            _tmpSender = _cursor.getString(_cursorIndexOfSender);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpSuggestedActionJson;
            if (_cursor.isNull(_cursorIndexOfSuggestedActionJson)) {
              _tmpSuggestedActionJson = null;
            } else {
              _tmpSuggestedActionJson = _cursor.getString(_cursorIndexOfSuggestedActionJson);
            }
            _item = new ChatMessageEntity(_tmpId,_tmpThreadId,_tmpSender,_tmpContent,_tmpTimestamp,_tmpSuggestedActionJson);
            _result.add(_item);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
