//
//  View+rowStyle.swift
//  iosApp
//
//  Created by Lubos Lehota on 07/04/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

extension View {
  func asStaticItem() -> some View {
    modifier(StaticItemStyle())
  }
}

struct StaticItemStyle: ViewModifier {
    func body(content: Content) -> some View {
      content
        .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
        .listRowBackground(Color.clear)
    }
}
