package edu.columbia.rdf.edb;

import java.util.Collection;
import java.util.Iterator;

public class Groups implements Iterable<Group> {
  private Collection<Group> mGroups;
  private boolean mAllMode;

  public Groups(Collection<Group> groups, boolean allMode) {
    mGroups = groups;
    mAllMode = allMode;
  }

  public Collection<Group> getGroups() {
    return mGroups;
  }

  public boolean getAllMode() {
    return mAllMode;
  }

  @Override
  public Iterator<Group> iterator() {
    return mGroups.iterator();
  }

  public int size() {
    return mGroups.size();
  }
}
