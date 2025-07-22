/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, Input, OnInit } from '@angular/core';
import { SideNavItem } from 'src/app/shared/models/side-nav-item';
import { SharedService } from 'src/app/core/services/shared.service';
import { ActiveLinkInfo } from 'src/app/shared/models/active-link-info';

@Component({
  selector: 'app-sidebar-inner-nav-item',
  templateUrl: './sidebar-inner-nav-item.component.html',
  styleUrls: ['./sidebar-inner-nav-item.component.scss'],
})
export class SidebarInnerNavItemComponent implements OnInit {
  @Input() navItem: SideNavItem;
  @Input() paginatedGroupKey = '';
  @Input() baseUrl: string;
  itemUrl: string;
  targetId: string;
  activeEmissionLabel = '';
  activeParentProcessId: number;
  collapsedKey = '';
  navigationStartUrl = '';

  constructor(private sharedService: SharedService) {}

  ngOnInit() {
    this.itemUrl = `${this.baseUrl}${this.navItem.url}`;
    this.targetId = `${this.navItem.baseUrl}${this.navItem.id}children`;
    this.collapsedKey = this.paginatedGroupKey + this.itemUrl;
    if (this.navItem) {
      const cachedCollapse = this.sharedService.getSideNavCollapsedMapEntry(this.collapsedKey);
      if (cachedCollapse == undefined) {
        this.sharedService.setSideNavCollapsedMapEntry(this.collapsedKey, false);
      }
    }

    this.sharedService.activeLinkInfoEmitted$.subscribe((activeLinkInfo: ActiveLinkInfo) => {
      if (activeLinkInfo) {
        this.activeEmissionLabel = activeLinkInfo.label;
        this.activeParentProcessId = activeLinkInfo.parentId;
      } else {
        this.activeEmissionLabel = '';
        this.activeParentProcessId = null;
      }
    });
  }

  getCollapsedMapEntry(): boolean {
    return this.sharedService.getSideNavCollapsedMapEntry(this.collapsedKey);
  }

  toggleCollapsedMapEntry() {
    this.sharedService.setSideNavCollapsedMapEntry(
      this.collapsedKey,
      !this.sharedService.getSideNavCollapsedMapEntry(this.collapsedKey)
    );
  }

}
