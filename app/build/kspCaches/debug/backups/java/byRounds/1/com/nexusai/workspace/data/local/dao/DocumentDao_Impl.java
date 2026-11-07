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
import com.nexusai.workspace.data.local.entity.DocumentEntity;
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
public final class DocumentDao_Impl implements DocumentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DocumentEntity> __insertionAdapterOfDocumentEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteDocument;

  public DocumentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDocumentEntity = new EntityInsertionAdapter<DocumentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `documents` (`id`,`title`,`fileUri`,`pageCount`,`fileSizeFormatted`,`extractedContent`,`addedTimestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DocumentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getFileUri());
        statement.bindLong(4, entity.getPageCount());
        statement.bindString(5, entity.getFileSizeFormatted());
        statement.bindString(6, entity.getExtractedContent());
        statement.bindLong(7, entity.getAddedTimestamp());
      }
    };
    this.__preparedStmtOfDeleteDocument = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM documents WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertDocument(final DocumentEntity document,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDocumentEntity.insertAndReturnId(document);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteDocument(final long docId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteDocument.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, docId);
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
          __preparedStmtOfDeleteDocument.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DocumentEntity>> getAllDocumentsFlow() {
    final String _sql = "SELECT * FROM documents ORDER BY addedTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"documents"}, new Callable<List<DocumentEntity>>() {
      @Override
      @NonNull
      public List<DocumentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfFileUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fileUri");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfFileSizeFormatted = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSizeFormatted");
          final int _cursorIndexOfExtractedContent = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedContent");
          final int _cursorIndexOfAddedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "addedTimestamp");
          final List<DocumentEntity> _result = new ArrayList<DocumentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DocumentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpFileUri;
            _tmpFileUri = _cursor.getString(_cursorIndexOfFileUri);
            final int _tmpPageCount;
            _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            final String _tmpFileSizeFormatted;
            _tmpFileSizeFormatted = _cursor.getString(_cursorIndexOfFileSizeFormatted);
            final String _tmpExtractedContent;
            _tmpExtractedContent = _cursor.getString(_cursorIndexOfExtractedContent);
            final long _tmpAddedTimestamp;
            _tmpAddedTimestamp = _cursor.getLong(_cursorIndexOfAddedTimestamp);
            _item = new DocumentEntity(_tmpId,_tmpTitle,_tmpFileUri,_tmpPageCount,_tmpFileSizeFormatted,_tmpExtractedContent,_tmpAddedTimestamp);
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
  public Object getDocumentById(final long docId,
      final Continuation<? super DocumentEntity> $completion) {
    final String _sql = "SELECT * FROM documents WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, docId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DocumentEntity>() {
      @Override
      @Nullable
      public DocumentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfFileUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fileUri");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfFileSizeFormatted = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSizeFormatted");
          final int _cursorIndexOfExtractedContent = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedContent");
          final int _cursorIndexOfAddedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "addedTimestamp");
          final DocumentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpFileUri;
            _tmpFileUri = _cursor.getString(_cursorIndexOfFileUri);
            final int _tmpPageCount;
            _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            final String _tmpFileSizeFormatted;
            _tmpFileSizeFormatted = _cursor.getString(_cursorIndexOfFileSizeFormatted);
            final String _tmpExtractedContent;
            _tmpExtractedContent = _cursor.getString(_cursorIndexOfExtractedContent);
            final long _tmpAddedTimestamp;
            _tmpAddedTimestamp = _cursor.getLong(_cursorIndexOfAddedTimestamp);
            _result = new DocumentEntity(_tmpId,_tmpTitle,_tmpFileUri,_tmpPageCount,_tmpFileSizeFormatted,_tmpExtractedContent,_tmpAddedTimestamp);
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
