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
import com.nexusai.workspace.data.local.entity.ActionCardEntity;
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
public final class ActionCardDao_Impl implements ActionCardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ActionCardEntity> __insertionAdapterOfActionCardEntity;

  private final EntityDeletionOrUpdateAdapter<ActionCardEntity> __updateAdapterOfActionCardEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCard;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllCards;

  public ActionCardDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfActionCardEntity = new EntityInsertionAdapter<ActionCardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `action_cards` (`id`,`title`,`description`,`cardType`,`status`,`dueDate`,`checklistItemsJson`,`checklistCompletedJson`,`createdTimestamp`,`sourceMessageId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ActionCardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getCardType());
        statement.bindString(5, entity.getStatus());
        if (entity.getDueDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDueDate());
        }
        statement.bindString(7, entity.getChecklistItemsJson());
        statement.bindString(8, entity.getChecklistCompletedJson());
        statement.bindLong(9, entity.getCreatedTimestamp());
        if (entity.getSourceMessageId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getSourceMessageId());
        }
      }
    };
    this.__updateAdapterOfActionCardEntity = new EntityDeletionOrUpdateAdapter<ActionCardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `action_cards` SET `id` = ?,`title` = ?,`description` = ?,`cardType` = ?,`status` = ?,`dueDate` = ?,`checklistItemsJson` = ?,`checklistCompletedJson` = ?,`createdTimestamp` = ?,`sourceMessageId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ActionCardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getCardType());
        statement.bindString(5, entity.getStatus());
        if (entity.getDueDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDueDate());
        }
        statement.bindString(7, entity.getChecklistItemsJson());
        statement.bindString(8, entity.getChecklistCompletedJson());
        statement.bindLong(9, entity.getCreatedTimestamp());
        if (entity.getSourceMessageId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getSourceMessageId());
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteCard = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM action_cards WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllCards = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM action_cards";
        return _query;
      }
    };
  }

  @Override
  public Object insertCard(final ActionCardEntity card,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfActionCardEntity.insertAndReturnId(card);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCard(final ActionCardEntity card,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfActionCardEntity.handle(card);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCard(final long cardId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCard.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
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
          __preparedStmtOfDeleteCard.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllCards(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllCards.acquire();
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
          __preparedStmtOfDeleteAllCards.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ActionCardEntity>> getAllCardsFlow() {
    final String _sql = "SELECT * FROM action_cards ORDER BY createdTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"action_cards"}, new Callable<List<ActionCardEntity>>() {
      @Override
      @NonNull
      public List<ActionCardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCardType = CursorUtil.getColumnIndexOrThrow(_cursor, "cardType");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfChecklistItemsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "checklistItemsJson");
          final int _cursorIndexOfChecklistCompletedJson = CursorUtil.getColumnIndexOrThrow(_cursor, "checklistCompletedJson");
          final int _cursorIndexOfCreatedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTimestamp");
          final int _cursorIndexOfSourceMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageId");
          final List<ActionCardEntity> _result = new ArrayList<ActionCardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ActionCardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpCardType;
            _tmpCardType = _cursor.getString(_cursorIndexOfCardType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final String _tmpChecklistItemsJson;
            _tmpChecklistItemsJson = _cursor.getString(_cursorIndexOfChecklistItemsJson);
            final String _tmpChecklistCompletedJson;
            _tmpChecklistCompletedJson = _cursor.getString(_cursorIndexOfChecklistCompletedJson);
            final long _tmpCreatedTimestamp;
            _tmpCreatedTimestamp = _cursor.getLong(_cursorIndexOfCreatedTimestamp);
            final Long _tmpSourceMessageId;
            if (_cursor.isNull(_cursorIndexOfSourceMessageId)) {
              _tmpSourceMessageId = null;
            } else {
              _tmpSourceMessageId = _cursor.getLong(_cursorIndexOfSourceMessageId);
            }
            _item = new ActionCardEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCardType,_tmpStatus,_tmpDueDate,_tmpChecklistItemsJson,_tmpChecklistCompletedJson,_tmpCreatedTimestamp,_tmpSourceMessageId);
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
  public Flow<List<ActionCardEntity>> getCardsByTypeFlow(final String type) {
    final String _sql = "SELECT * FROM action_cards WHERE cardType = ? ORDER BY createdTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"action_cards"}, new Callable<List<ActionCardEntity>>() {
      @Override
      @NonNull
      public List<ActionCardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCardType = CursorUtil.getColumnIndexOrThrow(_cursor, "cardType");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfChecklistItemsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "checklistItemsJson");
          final int _cursorIndexOfChecklistCompletedJson = CursorUtil.getColumnIndexOrThrow(_cursor, "checklistCompletedJson");
          final int _cursorIndexOfCreatedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTimestamp");
          final int _cursorIndexOfSourceMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageId");
          final List<ActionCardEntity> _result = new ArrayList<ActionCardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ActionCardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpCardType;
            _tmpCardType = _cursor.getString(_cursorIndexOfCardType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final String _tmpChecklistItemsJson;
            _tmpChecklistItemsJson = _cursor.getString(_cursorIndexOfChecklistItemsJson);
            final String _tmpChecklistCompletedJson;
            _tmpChecklistCompletedJson = _cursor.getString(_cursorIndexOfChecklistCompletedJson);
            final long _tmpCreatedTimestamp;
            _tmpCreatedTimestamp = _cursor.getLong(_cursorIndexOfCreatedTimestamp);
            final Long _tmpSourceMessageId;
            if (_cursor.isNull(_cursorIndexOfSourceMessageId)) {
              _tmpSourceMessageId = null;
            } else {
              _tmpSourceMessageId = _cursor.getLong(_cursorIndexOfSourceMessageId);
            }
            _item = new ActionCardEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCardType,_tmpStatus,_tmpDueDate,_tmpChecklistItemsJson,_tmpChecklistCompletedJson,_tmpCreatedTimestamp,_tmpSourceMessageId);
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
  public Object getCardById(final long cardId,
      final Continuation<? super ActionCardEntity> $completion) {
    final String _sql = "SELECT * FROM action_cards WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cardId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ActionCardEntity>() {
      @Override
      @Nullable
      public ActionCardEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCardType = CursorUtil.getColumnIndexOrThrow(_cursor, "cardType");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfChecklistItemsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "checklistItemsJson");
          final int _cursorIndexOfChecklistCompletedJson = CursorUtil.getColumnIndexOrThrow(_cursor, "checklistCompletedJson");
          final int _cursorIndexOfCreatedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTimestamp");
          final int _cursorIndexOfSourceMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageId");
          final ActionCardEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpCardType;
            _tmpCardType = _cursor.getString(_cursorIndexOfCardType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final String _tmpChecklistItemsJson;
            _tmpChecklistItemsJson = _cursor.getString(_cursorIndexOfChecklistItemsJson);
            final String _tmpChecklistCompletedJson;
            _tmpChecklistCompletedJson = _cursor.getString(_cursorIndexOfChecklistCompletedJson);
            final long _tmpCreatedTimestamp;
            _tmpCreatedTimestamp = _cursor.getLong(_cursorIndexOfCreatedTimestamp);
            final Long _tmpSourceMessageId;
            if (_cursor.isNull(_cursorIndexOfSourceMessageId)) {
              _tmpSourceMessageId = null;
            } else {
              _tmpSourceMessageId = _cursor.getLong(_cursorIndexOfSourceMessageId);
            }
            _result = new ActionCardEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCardType,_tmpStatus,_tmpDueDate,_tmpChecklistItemsJson,_tmpChecklistCompletedJson,_tmpCreatedTimestamp,_tmpSourceMessageId);
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
