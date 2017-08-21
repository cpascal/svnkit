/*
 * ====================================================================
 * Copyright (c) 2004-2012 TMate Software Ltd.  All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.  The terms
 * are also available at http://svnkit.com/license.html
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
package org.tmatesoft.svn.core.wc;

import java.util.HashMap;
import java.util.Map;

/**
 * The <b>SVNEventAction</b> class is used to describe an action
 * which generated an <b>SVNEvent</b> object.
 * <p>
 * Each operation invoked by
 * a do*() method of an <b>SVN</b>*<b>Client</b> class consists of
 * several actions that can be considered as operation steps. For example,
 * an update operation receives changes for files, adds new ones, deletes
 * another ones and so on. And for every such action (for every file
 * updated, deleted, added, etc.) the
 * {@link SVNUpdateClient#doUpdate(java.io.File, SVNRevision, org.tmatesoft.svn.core.SVNDepth, boolean, boolean) doUpdate()}
 * method generates an <b>SVNEvent</b> objects which contains information
 * on the type of this action that can be retrieved simply calling
 * the <b>SVNEvent</b>'s {@link SVNEvent#getAction() getAction()} method:
 * <pre class="javacode">
 * <span class="javakeyword">import</span> org.tmatesoft.svn.core.wc.SVNEvent;
 * <span class="javakeyword">import</span> org.tmatesoft.svn.core.wc.SVNEventAction;
 * ...
 *
 *   SVNEventAction action = event.getAction();
 *   <span class="javacomment">//parse the action according to the type of</span>
 *   <span class="javacomment">//operation and your needs</span>
 *   <span class="javakeyword">if</span> (action == SVNEventAction.UPDATE_UPDATE){
 *       ...
 *   }
 *   ...</pre>
 * <p>
 * <b>SVNEventAction</b> is just a set of predefined constant fields of
 * the same type. Each constant is applicable only to a certain type
 * of operation - for example those constants that names start with the
 * <i>UPDATE_</i> prefix are relevant only for update related operations
 * (update, checkout, switch, etc.).
 *
 * @version 1.3
 * @author  TMate Software Ltd.
 * @since   1.2
 * @see     SVNEvent
 * @see     ISVNEventHandler
 * @see     <a target="_top" href="http://svnkit.com/kb/examples/">Examples</a>
 */
public class SVNEventAction {

    public static SVNEventAction getEventActionById(int id) {
        synchronized (allActions) {
            return allActions.get(id);
        }
    }

    private static SVNEventAction createEventAction(int id, String name) {
        SVNEventAction eventAction = new SVNEventAction(id, name);
        synchronized (allActions) {
            allActions.put(id, eventAction);
        }
        return eventAction;
    }

    private static Map<Integer, SVNEventAction> allActions = new HashMap<Integer, SVNEventAction>();

    private int myID;
    private String myName;

    private SVNEventAction(int id, String name) {
        myID = id;
        myName = name;
    }

    /**
     * Returns this object's identifier.
     * Each constant field of the <b>SVNEventAction</b> class is also an
     * <b>SVNEventAction</b> object with its own id.
     *
     * @return id of this object
     */
    public int getID() {
        return myID;
    }

    /**
     * Returns a string representation of this object.
     * As a matter of fact this is a string representation of this
     * object's id.
     *
     * @return a string representing this object
     */
    public String toString() {
        return myName == null ? Integer.toString(myID) : myName;
    }

    /**
     * Reserved for future purposes.
     */
    public static final SVNEventAction PROGRESS = createEventAction(-1, "progress");

    /**
     * Denotes that a new item is scheduled for addition. Generated
     * by the {@link SVNWCClient#doAdd(java.io.File, boolean, boolean, boolean, org.tmatesoft.svn.core.SVNDepth, boolean, boolean) doAdd()}
     * method.
     */
    public static final SVNEventAction ADD = createEventAction(0, "add");

    /**
     * Denotes that the item is copied with history.
     *
     * @see SVNCopyClient
     */
    public static final SVNEventAction COPY = createEventAction(1, "copy");

    /**
     * Denotes that the item is scheduled for deletion. Generated
     * by the {@link SVNWCClient#doDelete(java.io.File, boolean, boolean) doDelete()}
     * method.
     */
    public static final SVNEventAction DELETE = createEventAction(2, "delete");

    /**
     * Denotes that the deleted item is restored (prior to be updated).
     */
    public static final SVNEventAction RESTORE = createEventAction(3, "restore");

    /**
     * Denotes that all local changes to the item were reverted. Generated by
     * the {@link SVNWCClient#doRevert(java.io.File[], org.tmatesoft.svn.core.SVNDepth, java.util.Collection) doRevert()}
     * method.
     */
    public static final SVNEventAction REVERT = createEventAction(4, "revert");

    /**
     * Denotes that a revert operation failed. Generated by the
     * {@link SVNWCClient#doRevert(java.io.File[], org.tmatesoft.svn.core.SVNDepth, java.util.Collection) doRevert()} method.
     */
    public static final SVNEventAction FAILED_REVERT = createEventAction(5, "failed_revert");

    /**
     * Denotes that the conflict on the item is resolved (the item is
     * marked resolved). Such an event is generated by the
     * {@link SVNWCClient#doResolve(java.io.File, org.tmatesoft.svn.core.SVNDepth, SVNConflictChoice) doResolve()} method.
     */
    public static final SVNEventAction RESOLVED = createEventAction(6, "resolved");

    /**
     * Denotes that the operation is skipped due to errors (inability to
     * be performed, etc.).
     */
    public static final SVNEventAction SKIP = createEventAction(7, "skip");

    /**
     * In an update operation denotes that the item is deleted from
     * the Working Copy (as it was deleted in the repository).
     */
    public static final SVNEventAction UPDATE_DELETE = createEventAction(8, "update_delete");

    /**
     * In an update operation denotes that the item is added to
     * the Working Copy (as it was added in the repository).
     */
    public static final SVNEventAction UPDATE_ADD = createEventAction(9, "update_add");

    /**
     * In an update operation denotes that the item is modified (there
     * are changes received from the repository).
     *
     */
    public static final SVNEventAction UPDATE_UPDATE = createEventAction(10, "update_update");

    /**
     * In an update operation denotes that the item is not modified, but its children are.
     *
     */
    public static final SVNEventAction UPDATE_NONE = createEventAction(-10, "update_none");

    /**
     * In an update operation denotes that the operation itself is completed
     * (for instance, in a console client can be used to print out the
     * revision updated to).
     */
    public static final SVNEventAction UPDATE_COMPLETED = createEventAction(11, "update_completed");

    /**
     * In an update operation denotes that the item being updated is
     * external.
     */
    public static final SVNEventAction UPDATE_EXTERNAL = createEventAction(12, "update_external");

    /**
     * In a remote status operation denotes that the operation itself is completed -
     * used to get the latest repository revision against which the status was
     * invoked.
     */
    public static final SVNEventAction STATUS_COMPLETED = createEventAction(13, "status_completed");

    /**
     * In a status operation denotes that the status is performed on an
     * external item. To find out the item's current status use
     * {@link SVNEvent#getContentsStatus() getContentsStatus()},
     * {@link SVNEvent#getPropertiesStatus() getPropertiesStatus()}.
     * The {@link SVNStatusType#STATUS_EXTERNAL} constant says only that the
     * item belongs to externals definitions.
     *
     */
    public static final SVNEventAction STATUS_EXTERNAL = createEventAction(14, "status_external");

    /**
     * In a commit operation denotes sending the item's modifications to the
     * repository.
     */
    public static final SVNEventAction COMMIT_MODIFIED = createEventAction(15, "commit_modified");

    /**
     * In a commit operation denotes adding a new item to the repository.
     */
    public static final SVNEventAction COMMIT_ADDED = createEventAction(16, "commit_added");

    /**
     * In a commit operation denotes deleting the item from the
     * repository.
     */
    public static final SVNEventAction COMMIT_DELETED = createEventAction(17, "commit_deleted");

    /**
     * In a commit operation denotes replacing (one item was deleted while
     * another one with the same name was added) the item in the repository.
     */
    public static final SVNEventAction COMMIT_REPLACED = createEventAction(18, "commit_replaced");

    /**
     * In a commit operation denotes the final stage of the operation -
     * sending all file data and finalizing the commit.
     */
    public static final SVNEventAction COMMIT_DELTA_SENT = createEventAction(19, "commit_delta_sent");

    /**
     * In a commit operation denotes that the operation itself is completed
     * (for instance, in a console client can be used to print out the
     * committed revision).
     */
    public static final SVNEventAction COMMIT_COMPLETED = createEventAction(-3, "commit_completed");

    /**
     * Denotes that file blaming is started.
     */
    public static final SVNEventAction ANNOTATE = createEventAction(20, "annotate");

    /**
     * Denotes that the file item is locked as a result of a locking
     * operation. Generated by a <b>doLock()</b> method of {@link SVNWCClient}.
     */
    public static final SVNEventAction LOCKED = createEventAction(21, "locked");

    /**
     * Denotes that the file item is unlocked as a result of an unlocking
     * operation. Generated by a <b>doUnlock()</b> method of {@link SVNWCClient}.
     */
    public static final SVNEventAction UNLOCKED = createEventAction(22, "unlocked");

    /**
     * Denotes that locking a file item failed. Generated by a <b>doLock()</b>
     * method of {@link SVNWCClient}.
     */
    public static final SVNEventAction LOCK_FAILED = createEventAction(23, "lock_failed");

    /**
     * Denotes that unlocking a file item failed. Generated by a <b>doUnlock()</b>
     * method of {@link SVNWCClient}.
     */
    public static final SVNEventAction UNLOCK_FAILED = createEventAction(24, "unlock_failed");

    /**
     * Denotes that the current format of the working copy administrative
     * area is upgraded to a newer one.
     */
    public static final SVNEventAction UPGRADE = createEventAction(-2, "wc_upgrade");

    /**
     * An working copy directory was upgraded to the latest format
     * @since New in 1.7. 
     */
    public static final SVNEventAction UPGRADED_PATH = createEventAction(50, "upgraded_path");

    /**
     * The server has instructed the client to follow a URL redirection.
     * @since SVN 1.7.
     */
    public static final SVNEventAction URL_REDIRECT = createEventAction(59, "url_redirect");

    /**
     * Denotes that tried adding a path that already exists.
     * @since 1.2.0, SVN 1.5.0
     */
    public static final SVNEventAction UPDATE_EXISTS = createEventAction(25, "update_exists");

    /**
     * Denotes that changelist name is set.
     * @since 1.2.0, SVN 1.5.0
     */
    public static final SVNEventAction CHANGELIST_SET = createEventAction(26, "changelist_set");

    /**
     * Denotes that changelist name is cleared.
     * @since 1.2.0, SVN 1.5.0
     */
    public static final SVNEventAction CHANGELIST_CLEAR = createEventAction(27, "changelist_clear");

    /**
     * Denotes that a path has moved from one changelist to another.
     * @since 1.2.0, SVN 1.5.0
     */
    public static final SVNEventAction CHANGELIST_MOVED = createEventAction(28, "changelist_moved");

    /**
     * Denotes that a merge operation (to path) has begun. See {@link SVNEvent#getMergeRange()}.
     * @since 1.2.0, SVN 1.5.0
     */
    public static final SVNEventAction MERGE_BEGIN = createEventAction(29, "merge_begin");

    /**
     * Denotes that a merge operation (to path) from a foreign repository has begun.
     * See {@link SVNEvent#getMergeRange()}.
     * @since 1.2.0, SVN 1.5.0
     */
    public static final SVNEventAction FOREIGN_MERGE_BEGIN = createEventAction(30, "foreign_merge_begin");

    /**
     * Denotes a replace notification.
     * @since 1.2.0, SVN 1.5.0
     */
    public static final SVNEventAction UPDATE_REPLACE = createEventAction(31, "update_replace");
    /**
     * @since 1.3, SVN 1.6
     */
    public static final SVNEventAction PROPERTY_ADD = createEventAction(32, "property_added");
    /**
     * @since 1.3, SVN 1.6
     */
    public static final SVNEventAction PROPERTY_MODIFY = createEventAction(33, "property_modified");
    /**
     * @since 1.3, SVN 1.6
     */
    public static final SVNEventAction PROPERTY_DELETE = createEventAction(34, "property_deleted");
    /**
     * @since 1.3, SVN 1.6
     */
    public static final SVNEventAction PROPERTY_DELETE_NONEXISTENT = createEventAction(35, "property_deleted_nonexistent");
    /**
     * @since 1.3, SVN 1.6
     */
    public static final SVNEventAction REVPROPER_SET = createEventAction(36, "revprop_set");
    /**
     * @since 1.3, SVN 1.6
     */
    public static final SVNEventAction REVPROP_DELETE = createEventAction(37, "revprop_deleted");
    /**
     * @since 1.3, SVN 1.6
     */
    public static final SVNEventAction MERGE_COMPLETE = createEventAction(38, "merge_completed");
    /**
     * @since 1.3, SVN 1.6
     */
    public static final SVNEventAction TREE_CONFLICT = createEventAction(39, "tree_conflict");

    /**
     * @since 1.3, SVN 1.6
     */
    public static final SVNEventAction FAILED_EXTERNAL = createEventAction(40, "failed_external");

    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction PATCH = createEventAction(53, "patch");

    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction UPDATE_STARTED = createEventAction(41, "update_started");
    
    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction PATCH_REJECTED_HUNK = createEventAction(55, "patch_rejected_hunk");

    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction PATCH_APPLIED_HUNK = createEventAction(54, "patch_applied_hunk");

    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction PATCH_HUNK_ALREADY_APPLIED = createEventAction(56, "patch_hunk_already_applied");

    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction UPDATE_SKIP_OBSTRUCTION = createEventAction(42, "update_skip_obstruction");
    
    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction UPDATE_SKIP_WORKING_ONLY = createEventAction(43, "update_skip_working_only");

    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction UPDATE_SKIP_ACCESS_DENINED = createEventAction(44, "update_skip_access_denied");
    
    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction UPDATE_EXTERNAL_REMOVED = createEventAction(45, "update_external_removed");

    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction UPDATE_SHADOWED_ADD = createEventAction(46, "update_shadowed_add");

    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction UPDATE_SHADOWED_UPDATE = createEventAction(47, "update_shadowed_update");

    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction UPDATE_SHADOWED_DELETE = createEventAction(48, "update_shadowed_delete");

    /**
     * @since 1.4, SVN 1.7
     */
    public static final SVNEventAction SKIP_CONFLICTED = createEventAction(68, "skip_conflicted");

    /**
     * @since 1.7, SVN 1.7
     */
    public static final SVNEventAction PATH_NONEXISTENT = createEventAction(60, "path_nonexistent");

    /**
     * @since 1.7, SVN 1.7
     */
    public static final SVNEventAction MERGE_RECORD_INFO = createEventAction(49, "merge_record_info");

    /**
     * @since 1.7, SVN 1.7
     */
    public static final SVNEventAction MERGE_RECORD_INFO_BEGIN = createEventAction(51, "merge_record_info_begin");

    /**
     * @since 1.7, SVN 1.7
     */
    public static final SVNEventAction MERGE_ELIDE_INFO = createEventAction(52, "merge_elide_info");

    /**
     * @since 1.7, SVN 1.7
     */
    public static final SVNEventAction FAILED_OUT_OF_DATE = createEventAction(64, "failed_out_of_date");

    /**
     * @since 1.7, SVN 1.7
     */
    public static final SVNEventAction FAILED_NO_PARENT = createEventAction(65, "failed_no_parent");

    /**
     * @since 1.7, SVN 1.7
     */
    public static final SVNEventAction FAILED_LOCKED = createEventAction(66, "failed_locked");
    /**
     * @since 1.7, SVN 1.7
     */
    public static final SVNEventAction FAILED_FORBIDDEN_BY_SERVER = createEventAction(67, "failed_forbidden_by_server");

    /**
     * @since 1.8, SVN 1.8
     */
    public static final SVNEventAction UPDATE_BROKEN_LOCK = createEventAction(69, "update_broken_lock");

    /**
     * @since 1.8, SVN 1.8
     */
    public static final SVNEventAction RESOLVER_STARTING = createEventAction(71, "resolver_starting");

    /**
     * @since 1.8, SVN 1.8
     */
    public static final SVNEventAction RESOLVER_DONE = createEventAction(72, "resolver_done");

    /**
     * @since 1.8, SVN 1.8
     */
    public static final SVNEventAction FAILED_OBSTRUCTION = createEventAction(70, "failed_obstruction");

    /**
     * @since 1.7, SVN 1.7
     */
    public static final SVNEventAction FAILED_CONFLICT = createEventAction(62, "failed_conflict");

    /**
     * @since 1.7, SVN 1.7
     */
    public static final SVNEventAction FAILED_MISSING = createEventAction(63, "failed_missing");

    /**
     * @since 1.8, SVN 1.8
     */
    public static final SVNEventAction FOREIGN_COPY_BEGIN = createEventAction(74, "foreign_copy_begin");

    /**
     * @since 1.8, SVN 1.8
     */
    public static final SVNEventAction MOVE_BROKEN = createEventAction(75, "move_broken");

    /**
     * @since 1.9, SVN 1.9
     */
    public static final SVNEventAction CLEANUP_EXTERNAL = createEventAction(76, "cleanup_external");

    /**
     * @since 1.9, SVN 1.9
     */
    public static final SVNEventAction FAILED_REQUIRES_TARGET = createEventAction(77, "failed_requires_target");

    /**
     * @since 1.9, SVN 1.9
     */
    public static final SVNEventAction INFO_EXTERNAL = createEventAction(78, "info_external");

    /**
     * @since 1.9, SVN 1.9
     */
    public static final SVNEventAction COMMIT_FINALIZING = createEventAction(79, "commit_finalizing");
}